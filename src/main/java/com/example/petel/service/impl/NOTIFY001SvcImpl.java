package com.example.petel.service.impl;

import com.example.petel.component.NotificationHub;
import com.example.petel.dto.*;
import com.example.petel.entity.NotificationEventsEntity;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.UsersEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationEventsRepository;
import com.example.petel.repository.NotificationsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.NOTIFY001Svc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * NOTIFY-001 發送通知 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY001SvcImpl implements NOTIFY001Svc {

    private final NotificationsRepository notificationsRepository;
    private final NotificationEventsRepository notificationEventsRepository;
    private final NotificationHub notificationHub;
    private final ObjectMapper objectMapper;
    private final OrdersRepository ordersRepository;
    private final PropertyRepository propertyRepository;
    private final UsersRepository usersRepository;

    /**
     * 發送通知給指定帳號
     *
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return Res<NOTIFY001Tranrs>
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<NOTIFY001Tranrs> notify001(String accountId, Req<NOTIFY001Tranrq> requestBody) {

        log.info("-------- [NOTIFY-001] 發送通知 API 啟動 --------");

        NOTIFY001Tranrq tranrq = requestBody.getTranrq();

        // 1) 使用 UUID 產生通知 ID
        String notificationId = "N" + UUID.randomUUID().toString().replace("-", "").substring(0, 9);

        // 2) 建立並儲存通知主表
        NotificationsEntity notification = new NotificationsEntity();
        notification.setId(notificationId);
        notification.setAccountId(accountId);
        notification.setTitle(tranrq.getTitle());
        notification.setMessage(tranrq.getMessage());
        notification.setType(tranrq.getType());
        notification.setStatus("UNREAD");
        notification.setCreatedAt(OffsetDateTime.now());
        notification.setOrderId(tranrq.getOrderId());
        notification.setPropertyId(tranrq.getPropertyId());
        notification.setSellerId(tranrq.getSellerId());
        notificationsRepository.save(notification);

        // 3) 使用 UUID 產生事件 ID
        String eventId = generateUniqueEventId(accountId);
        String eventPrimaryKey = "E" + UUID.randomUUID().toString().replace("-", "").substring(0, 9);

        // 建立 payload（包含通知的完整資訊）
        Map<String, Object> payloadData = new HashMap<>();
        payloadData.put("id", notificationId);
        payloadData.put("title", tranrq.getTitle());
        payloadData.put("message", tranrq.getMessage());
        payloadData.put("type", tranrq.getType());
        payloadData.put("status", "UNREAD");
        payloadData.put("createdAt", notification.getCreatedAt().toString());
        if (tranrq.getOrderId() != null) payloadData.put("orderId", tranrq.getOrderId());
        if (tranrq.getPropertyId() != null) payloadData.put("propertyId", tranrq.getPropertyId());
        if (tranrq.getSellerId() != null) payloadData.put("sellerId", tranrq.getSellerId());

        // 如果是 ORDER 類型且有 orderId，則查詢並加入 propertyName 和 userName
        if ("ORDER".equals(tranrq.getType()) && tranrq.getOrderId() != null) {
            try {
                Optional<OrdersEntity> orderOpt = ordersRepository.findById(tranrq.getOrderId());
                if (orderOpt.isPresent()) {
                    OrdersEntity order = orderOpt.get();

                    // 查詢並加入 Property Name
                    if (order.getPropertyId() != null) {
                        Optional<PropertyEntity> propertyOpt = propertyRepository.findById(order.getPropertyId());
                        if (propertyOpt.isPresent()) {
                            payloadData.put("propertyName", propertyOpt.get().getName());
                        }
                    }

                    // 查詢並加入 User Name
                    if (order.getUserId() != null) {
                        Optional<UsersEntity> userOpt = usersRepository.findById(order.getUserId());
                        if (userOpt.isPresent()) {
                            payloadData.put("userName", userOpt.get().getName());
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("[NOTIFY-001] 查詢訂單相關資料失敗，訂單ID: {}", tranrq.getOrderId(), e);
            }
        }

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(payloadData);
        } catch (JsonProcessingException e) {
            log.error("[NOTIFY-001] 序列化 payload 失敗", e);
            throw new RuntimeException("Failed to serialize notification payload", e);
        }

        NotificationEventsEntity event = new NotificationEventsEntity();
        event.setId(eventPrimaryKey); // UUID 作為 Primary Key
        event.setAccountId(accountId);
        event.setEventId(eventId); // SSE 的事件 ID
        event.setEventName("notification");
        event.setPayload(payloadJson);
        event.setSentAt(OffsetDateTime.now());
        event.setDelivered("N");
        event.setNotificationId(notificationId);
        notificationEventsRepository.save(event);

        // 4) 交易提交後再推播（確保資料已寫入資料庫）
        final String finalEventId = eventId;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    notificationHub.sendTo(accountId, "notification", finalEventId, payloadData);
                    // 標記為已送達
                    event.setDelivered("Y");
                    notificationEventsRepository.save(event);
                    log.info("[NOTIFY-001] 通知發送成功，通知ID：{}", notificationId);
                } catch (Exception e) {
                    // 發送失敗，保持 delivered = 'N'，供後續補發
                    log.error("[NOTIFY-001] 推播通知失敗，帳號ID：{}", accountId, e);
                }
            }
        });

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY001Tranrs(notificationId));
    }

    /**
     * 生成唯一的事件 ID（用於 SSE）
     * 格式：accountId-timestamp-random
     */
    private String generateUniqueEventId(String accountId) {
        return accountId + "-" + System.currentTimeMillis() + "-" +
               (int)(Math.random() * 10000);
    }
}
