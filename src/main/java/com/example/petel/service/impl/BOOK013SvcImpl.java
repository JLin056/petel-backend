package com.example.petel.service.impl;

import com.example.petel.dto.BOOK013Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.BOOK013Svc;
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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

/**
 * BOOK-013 模擬更新訂單付款狀態 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK013SvcImpl implements BOOK013Svc {

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
    /** UsersRepository */
    private final UsersRepository usersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000002";

    /**
     * 模擬更新訂單付款狀態 (線上刷卡)
     *
     * @param requestBody Req<BOOK013Tranrq>
     * @return Res<Object>
     * @throws DataNotFoundException 查無資料
     * @throws UpdateFailException 修改失敗
     * @throws InvalidPaymentMethodException 付款方式異常
     * @throws InvalidPaymentMethodException 新增失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<Object> book013(Req<BOOK013Tranrq> requestBody) throws DataNotFoundException, UpdateFailException, InvalidPaymentMethodException, InsertFailException {

        log.info("-------- [BOOK-013] 模擬更新訂單付款狀態 (線上刷卡) API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-013] 查無訂單編號為 {} 的資料", orderId);
                    return new DataNotFoundException();
                });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.warn("[BOOK-013] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        ordersEntity.setStatus("已付款");
        ordersEntity.setUpdatedAt(now);

        TransactionsEntity transactionsEntity = new TransactionsEntity();

        transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
        transactionsEntity.setOrderId(orderId);
        transactionsEntity.setPaymentId(PAYMENT_ID);
        transactionsEntity.setTransactionId("PETEL" + orderId);
        transactionsEntity.setFlowType("Pay");
        transactionsEntity.setHotelCharges(ordersEntity.getHotelCharges());
        transactionsEntity.setCreatedAt(now);
        transactionsEntity.setStatus("付款成功");
        transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
        transactionsEntity.setTransactionFee(0);
        transactionsEntity.setPayTime(now);

        try {
            ordersRepository.save(ordersEntity);
        } catch (Exception e) {
            log.error("[BOOK-013] 更新過程有問題，修改訂單編號為 {} 的訂單資料失敗", orderId);
            throw new UpdateFailException();
        }

        try {
            transactionsRepository.save(transactionsEntity);
        } catch (Exception e) {
            log.error("[BOOK-013] 新增資料有問題，新增訂單編號為 {} 的交易資料失敗", orderId);
            throw new InsertFailException();
        }

        // 發送訂單確認通知（將 USER_ID 轉換為 ACCOUNT_ID）
        String accountId = usersRepository.findByAccountByUserId(ordersEntity.getUserId());
        if (accountId != null) {
            sendNotification(accountId, "訂單確認", "您的訂單已確認", "ORDER", orderId);
            sendNotification(accountId, "付款成功", "您已成功支付 " + ordersEntity.getHotelCharges(), "PAYMENT", orderId);
        } else {
            log.warn("[BOOK-013] 無法找到 userId={} 對應的 accountId，通知發送失敗", ordersEntity.getUserId());
        }

        log.info("[BOOK-013] 模擬更新訂單付款狀態 (線上刷卡) API 執行完成");

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }

    /**
     * 發送通知
     *
     * @param accountId 帳號 ID
     * @param title 通知標題
     * @param message 通知訊息
     * @param type 通知類型
     * @param orderId 訂單編號
     */
    private void sendNotification(String accountId, String title, String message, String type, String orderId) {
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
            java.util.Map<String, Object> payloadData = new HashMap<>();
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
                        log.info("[BOOK-013] 通知發送成功，通知ID：{}", notificationId);
                    } catch (Exception e) {
                        log.error("[BOOK-013] 推播通知失敗，帳號ID：{}", accountId, e);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            log.error("[BOOK-013] 發送通知失敗", e);
        }
    }
}