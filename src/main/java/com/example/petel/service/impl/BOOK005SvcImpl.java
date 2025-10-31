package com.example.petel.service.impl;

import com.example.petel.component.NotificationHub;
import com.example.petel.dto.*;
import com.example.petel.entity.NotificationEventsEntity;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.*;
import com.example.petel.service.BOOK005Svc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * BOOK-005 組合付款參數 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK005SvcImpl implements BOOK005Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000001";

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


//    /** MERCHANT_ID */
//    @Value("${ecpay.merchantId}")
//    private String MERCHANT_ID;
//    /** HASH_KEY */
//    @Value("${ecpay.hashKey}")
//    private String HASH_KEY;
//    /** HASH_IV */
//    @Value("${ecpay.hashIv}")
//    private String HASH_IV;
//    /** RETURN_URL */
//    @Value("${ecpay.authorize.returnUrl}")
//    private String RETURN_URL;
//    /** ITEM_NAME */
//    private static final String ITEM_NAME = "PETEL room(s)";
//    /** RestTemplate */
//    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 組合付款參數 for 現場付款：信用卡預先授權
     *
     * @param requestBody Req<BOOK005Tranrq>
     * @return Res<BOOK005Tranrs>
     * @throws Exception 拋出例外
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOK005Tranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception {

        log.info("-------- [BOOK-005] 組合付款參數 (現場付款) API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-005] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
            return new DataNotFoundException();
        });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.warn("[BOOK-005] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

//        For company use, cancel the following code:

//        JSONObject orderInfoJson = new JSONObject();
//        orderInfoJson.put("MerchantTradeNo", "PETEL" + orderId);
//        orderInfoJson.put("MerchantTradeDate", TimeUtil.getMerchantTradeDate(ordersEntity.getCreatedAt()));
//        orderInfoJson.put("TotalAmount", ordersEntity.getHotelCharges());
//        orderInfoJson.put("ItemName", ITEM_NAME);
//        orderInfoJson.put("TradeDesc", "建立訂單");
//        orderInfoJson.put("ReturnURL", RETURN_URL);
//
//        BOOK005TranrqCardInfo cardInfo = requestBody.getTranrq().getCardInfo();
//        JSONObject cardInfoJson = new JSONObject();
//        cardInfoJson.put("CardNo", cardInfo.getCardNo());
//        cardInfoJson.put("CardValidMM", cardInfo.getCardValidMM());
//        cardInfoJson.put("CardValidYY", cardInfo.getCardValidYY());
//        cardInfoJson.put("CardCVV2", cardInfo.getCardCVV2());
//
//        BOOK005TranrqConsumerInfo consumerInfo = requestBody.getTranrq().getConsumerInfo();
//        JSONObject consumerInfoJson = new JSONObject();
//        consumerInfoJson.put("Phone", consumerInfo.getPhone());
//        consumerInfoJson.put("Name", consumerInfo.getName());
//
//        JSONObject dataJson = new JSONObject();
//        dataJson.put("MerchantID", MERCHANT_ID);
//        dataJson.put("OrderInfo", orderInfoJson);
//        dataJson.put("ChoosePayment", "Credit");
//        dataJson.put("CardInfo", cardInfoJson);
//        dataJson.put("ConsumerInfo", consumerInfoJson);
//
//        JSONObject rqHeader = new JSONObject();
//        rqHeader.put("Timestamp", ZonedDateTime.now(ZoneId.of("Asia/Taipei")).toEpochSecond());
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("MerchantID", MERCHANT_ID);
//        jsonObject.put("RqHeader", rqHeader);
//        jsonObject.put("Data", CodeUtil.dataEncrypt(dataJson.toString(), HASH_KEY, HASH_IV));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                "https://ecpayment-stage.ecpay.com.tw/1.0.0/Cashier/BackAuth",
//                request,
//                String.class);

        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        ordersEntity.setStatus("未付款");
        ordersEntity.setUpdatedAt(now);

        TransactionsEntity transactionsEntity = new TransactionsEntity();

        transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
        transactionsEntity.setOrderId(orderId);
        transactionsEntity.setPaymentId(PAYMENT_ID);
        transactionsEntity.setTransactionId("PETEL" + orderId); // 因為無法 call 幕後交易 api
        transactionsEntity.setFlowType("Pay");
        transactionsEntity.setHotelCharges(ordersEntity.getHotelCharges()); // 因為無法 call 幕後交易 api
        transactionsEntity.setCreatedAt(now);
        transactionsEntity.setStatus("授權成功");
        transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
        transactionsEntity.setTransactionFee(0);
        transactionsEntity.setPayTime(now);

        try {
            ordersRepository.save(ordersEntity);
        } catch (Exception e) {
            log.error("[BOOK-005] 更新過程有問題，修改訂單編號為 {} 的訂單資料失敗", orderId);
            throw new UpdateFailException();
        }

        try {
            transactionsRepository.save(transactionsEntity);
        } catch (Exception e) {
            log.error("[BOOK-005] 新增資料有問題，新增訂單編號為 {} 的交易資料失敗", orderId);
            throw new InsertFailException();
        }

        log.info("[BOOK-005] 訂單編號為 {} 的訂單，信用卡幕後授權 API 執行完成", orderId);

        // 發送訂單確認通知（將 USER_ID 轉換為 ACCOUNT_ID）
        String accountId = usersRepository.findByAccountByUserId(ordersEntity.getUserId());
        if (accountId != null) {
            sendNotification(accountId, "訂單確認", "您的訂單已確認", "ORDER", orderId);
//                sendNotification(accountId, "現場付款", "已授權信用卡", "PAYMENT", orderId);
        } else {
            log.warn("[BOOK-005] 無法找到 userId={} 對應的 accountId，通知發送失敗", ordersEntity.getUserId());
        }

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK005Tranrs("OK"));
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
                        log.info("[BOOK-005] 通知發送成功，通知ID：{}", notificationId);
                    } catch (Exception e) {
                        log.error("[BOOK-005] 推播通知失敗，帳號ID：{}", accountId, e);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            log.error("[BOOK-005] 發送通知失敗", e);
        }
    }


}

