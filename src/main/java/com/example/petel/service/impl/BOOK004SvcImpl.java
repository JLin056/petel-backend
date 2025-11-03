package com.example.petel.service.impl;

import com.example.petel.dto.BOOK004Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrderItemsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.RoomInventoriesEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrderItemsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.RoomInventoriesRepository;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.BOOK004Svc;
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
import java.util.List;

/**
 * BOOK-004 取消該筆訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK004SvcImpl implements BOOK004Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** OrderItemsRepository */
    private final OrderItemsRepository orderItemsRepository;
    /** RoomInventoriesRepository */
    private final RoomInventoriesRepository roomInventoriesRepository;
    /** RoomsRepository */
    private final RoomsRepository roomsRepository;
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
    /** PropertyRepository */
    private final PropertyRepository propertyRepository;
    /** SellersRepository */
    private final SellersRepository sellersRepository;

    /**
     * 取消該筆訂單
     *
     * @param requestBody Req<BOOK004Tranrq>
     * @return Res<Object>
     * @throws DataNotFoundException 查無資料
     * @throws DeleteFailException   刪除失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<Object> book004(Req<BOOK004Tranrq> requestBody) throws DataNotFoundException, DeleteFailException {

        log.info("-------- [BOOK-004] 取消該筆訂單 API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-004] 查無訂單編號為 {} 的資料，取消訂單失敗", orderId);
                    return new DataNotFoundException();
                });

        List<OrderItemsEntity> orderDetails = orderItemsRepository.findByOrderId(orderId);
        for (OrderItemsEntity orderDetail : orderDetails) {

            String roomId = orderDetail.getRoomId();

            RoomsEntity roomsEntity = roomsRepository.findById(roomId).orElseThrow(() -> {
                log.error("[BOOK-004] 查無房型編號 {} 資料，取消訂單失敗", roomId);
                return new DataNotFoundException();
            });

            RoomInventoriesEntity roomInventoriesEntity = roomInventoriesRepository.findByRoomIdAndStayDate(roomId, orderDetail.getArrivalDate()).orElseThrow(() -> {
                log.error("[BOOK-004] 查無庫存資料，取消訂單失敗");
                return new DataNotFoundException();
            });

            int newAvailableQuantity = roomInventoriesEntity.getAvailableQuantity() + orderDetail.getQuantity();

            if (newAvailableQuantity > roomsEntity.getTotalUnits()) {
                log.warn("[BOOK-004] 數據邏輯異常，請檢查");
                roomInventoriesEntity.setAvailableQuantity(roomsEntity.getTotalUnits());
            } else {
                roomInventoriesEntity.setAvailableQuantity(newAvailableQuantity);
            }
            roomInventoriesRepository.save(roomInventoriesEntity);
        }

        try {
            orderItemsRepository.deleteByOrderId(orderId);
        } catch (Exception e) {
            log.error("[BOOK-004] 訂單編號為 {} 的資料，刪除異常，取消訂單失敗", orderId);
            throw new DeleteFailException();
        }

        ordersEntity.setStatus("已取消");
        ordersEntity.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        ordersRepository.save(ordersEntity);

        // 發送訂單取消通知給會員（將 USER_ID 轉換為 ACCOUNT_ID）
        String userAccountId = usersRepository.findByAccountByUserId(ordersEntity.getUserId());
        if (userAccountId != null) {
            sendNotification(userAccountId, "訂單取消", "您的訂單已取消", "ORDER", orderId);
        } else {
            log.warn("[BOOK-004] 無法找到 userId={} 對應的 accountId，會員通知發送失敗", ordersEntity.getUserId());
        }

        // 發送訂單取消通知給商家（將 PROPERTY_ID 轉換為 SELLER_ID 再轉換為 ACCOUNT_ID）
        PropertyEntity propertyEntity = propertyRepository.findById(ordersEntity.getPropertyId()).orElse(null);
        if (propertyEntity != null) {
            String sellerAccountId = sellersRepository.findByAccountBySellerId(propertyEntity.getSellerId());
            if (sellerAccountId != null) {
                sendNotification(sellerAccountId, "訂單取消", "訂單編號 " + orderId + " 已被取消", "ORDER", orderId);
                log.info("[BOOK-004] 商家通知發送成功，訂單編號：{}", orderId);
            } else {
                log.warn("[BOOK-004] 無法找到 sellerId={} 對應的 accountId，商家通知發送失敗", propertyEntity.getSellerId());
            }
        } else {
            log.warn("[BOOK-004] 無法找到 propertyId={} 對應的旅館資料，商家通知發送失敗", ordersEntity.getPropertyId());
        }

        log.info("[BOOK-004] 訂單編號為 {} 的資料，取消訂單成功", orderId);
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
                        log.info("[BOOK-004] 通知發送成功，通知ID：{}", notificationId);
                    } catch (Exception e) {
                        log.error("[BOOK-004] 推播通知失敗，帳號ID：{}", accountId, e);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            log.error("[BOOK-004] 發送通知失敗", e);
        }
    }
}