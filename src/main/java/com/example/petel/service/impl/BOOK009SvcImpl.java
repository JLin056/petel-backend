package com.example.petel.service.impl;

import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.model.IdUtil;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.service.BOOK009Svc;
import com.example.petel.component.NotificationHub;
import com.example.petel.entity.NotificationEventsEntity;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.repository.NotificationEventsRepository;
import com.example.petel.repository.NotificationsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

/**
 * BOOK-009 綠界通知付款回應 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK009SvcImpl implements BOOK009Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** NotificationsRepository */
    private final NotificationsRepository notificationsRepository;
    /** NotificationEventsRepository */
    private final NotificationEventsRepository notificationEventsRepository;
    /** NotificationHub */
    private final NotificationHub notificationHub;
    /** ObjectMapper */
    private final ObjectMapper objectMapper;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000002";
    /** SUCCESS_RTN_CODE */
    private static final int SUCCESS_RTN_CODE = 1;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;
    /** PAYING_LOCK */
    private static final Object PAYING_LOCK = new Object();

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String book009(Map<String, Object> requestParam) throws Exception {

        log.info("-------- [BOOK-009] 綠界通知付款回應 (線上刷卡) API 啟動 --------");

        synchronized (PAYING_LOCK) {

            TransactionsEntity transactionsEntity = new TransactionsEntity();
            String orderId = requestParam.get("MerchantTradeNo").toString().replaceFirst("^PETEL", "");

            if (transactionsRepository.findByOrderId(orderId).isPresent()) {
                transactionsEntity = transactionsRepository.findByOrderId(orderId).get();
            } else {
                transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
                transactionsEntity.setOrderId(orderId);
                transactionsEntity.setPaymentId(PAYMENT_ID);
                transactionsEntity.setTransactionId(requestParam.get("TradeNo").toString());
                transactionsEntity.setFlowType("Pay");
                transactionsEntity.setHotelCharges(Integer.parseInt(requestParam.get("TradeAmt").toString()));
                transactionsEntity.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
            }

            if (Integer.parseInt(requestParam.get("RtnCode").toString()) != SUCCESS_RTN_CODE) {
                transactionsEntity.setStatus("付款失敗");
                transactionsRepository.save(transactionsEntity);
                OrdersEntity ordersEntity = ordersRepository.findById(orderId).get();
                ordersEntity.setStatus("未付款");
                ordersEntity.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
                ordersRepository.save(ordersEntity);
                log.warn("[BOOK-009] 付款失敗");
                return "1|OK";
            }

            if (!CodeUtil.generateCheckMacValue(requestParam, HASH_KEY, HASH_IV).equals((requestParam.get("CheckMacValue").toString()))) {
                transactionsEntity.setStatus("付款異常");
                transactionsRepository.save(transactionsEntity);
                OrdersEntity ordersEntity = ordersRepository.findById(orderId).get();
                ordersEntity.setStatus("未付款");
                ordersEntity.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
                ordersRepository.save(ordersEntity);
                log.warn("[BOOK-009] 檢查碼相異");
                return "1|OK";
            }

            transactionsEntity.setStatus("付款成功");
            transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
            if (requestParam.get("PaymentTypeChargeFee") == null) {
                transactionsEntity.setTransactionFee(0);
            } else {
                transactionsEntity.setTransactionFee(Integer.parseInt(requestParam.get("PaymentTypeChargeFee").toString()));
            }
            transactionsEntity.setPayTime(TimeUtil.parseMerchantTradeDate(requestParam.get("TradeDate").toString()));
            transactionsRepository.save(transactionsEntity);
            OrdersEntity ordersEntity = ordersRepository.findById(orderId).get();
            ordersEntity.setStatus("已付款");
            ordersEntity.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
            ordersRepository.save(ordersEntity);

            // 發送訂單確認通知
            sendNotification(ordersEntity.getUserId(), "訂單確認", "您的訂單已確認", "ORDER", orderId, null);

            // 發送付款成功通知
            sendNotification(ordersEntity.getUserId(), "付款成功", "您已成功支付 " + ordersEntity.getHotelCharges(), "PAYMENT", orderId, null);
        }

        log.info("[BOOK-009] 綠界通知付款回應 (線上刷卡) API 運行成功");

        return "1|OK";
    }

    /**
     * 發送通知
     *
     * @param accountId 帳號 ID
     * @param title 通知標題
     * @param message 通知訊息
     * @param type 通知類型
     * @param orderId 訂單編號
     * @param amount 金額（可選）
     */
    private void sendNotification(String accountId, String title, String message, String type, String orderId, Integer amount) {
        try {
            // 使用 UUID 產生通知 ID
            String notificationId = "N" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 9);

            // 建立並儲存通知主表
            NotificationsEntity notification = new NotificationsEntity();
            notification.setId(notificationId);
            notification.setAccountId(accountId);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(type);
            notification.setStatus("UNREAD");
            notification.setCreatedAt(java.time.OffsetDateTime.now());
            notification.setOrderId(orderId);
            notificationsRepository.save(notification);

            // 使用 UUID 產生事件 ID
            String eventId = accountId + "-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
            String eventPrimaryKey = "E" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 9);

            // 建立 payload
            java.util.Map<String, Object> payloadData = new java.util.HashMap<>();
            payloadData.put("id", notificationId);
            payloadData.put("title", title);
            payloadData.put("message", message);
            payloadData.put("type", type);
            payloadData.put("status", "UNREAD");
            payloadData.put("createdAt", notification.getCreatedAt().toString());
            payloadData.put("orderId", orderId);

            String payloadJson = objectMapper.writeValueAsString(payloadData);

            NotificationEventsEntity event = new NotificationEventsEntity();
            event.setId(eventPrimaryKey);
            event.setAccountId(accountId);
            event.setEventId(eventId);
            event.setEventName("notification");
            event.setPayload(payloadJson);
            event.setSentAt(java.time.OffsetDateTime.now());
            event.setDelivered("N");
            event.setNotificationId(notificationId);
            notificationEventsRepository.save(event);

            // 交易提交後再推播
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        notificationHub.sendTo(accountId, "notification", eventId, payloadData);
                        event.setDelivered("Y");
                        notificationEventsRepository.save(event);
                        log.info("[BOOK-009] 通知發送成功，通知ID：{}", notificationId);
                    } catch (Exception e) {
                        log.error("[BOOK-009] 推播通知失敗，帳號ID：{}", accountId, e);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            log.error("[BOOK-009] 發送通知失敗", e);
        }
    }
}