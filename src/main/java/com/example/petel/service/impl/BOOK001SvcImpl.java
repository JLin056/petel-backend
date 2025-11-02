package com.example.petel.service.impl;

import com.example.petel.component.NotificationHub;
import com.example.petel.dto.*;
import com.example.petel.entity.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.*;
import com.example.petel.service.BOOK001Svc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BOOK-001 建立訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK001SvcImpl implements BOOK001Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** OrderItemsRepository */
    private final OrderItemsRepository orderItemsRepository;
    /** RoomInventoriesRepository */
    private final RoomInventoriesRepository roomInventoriesRepository;
    /** RoomsRepository */
    private final RoomsRepository roomsRepository;
    /** UsersRepository */
    private final UsersRepository usersRepository;
    /** BOOKING_LOCK */
    private static final Object BOOKING_LOCK = new Object();

    /** NotificationsRepository */
    private final NotificationsRepository notificationsRepository;

    /** NotificationEventsRepository */
    private final NotificationEventsRepository notificationEventsRepository;

    /** NotificationHub */
    private final NotificationHub notificationHub;

    /** ObjectMapper */
    private final ObjectMapper objectMapper;




    /**
     * 建立訂單
     *
     * @param requestBody Req<BOOK001Tranrq>
     * @return Res<BOOK001Tranrs>
     * @throws InsertFailException 新增失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOK001Tranrs> book001(String accountId, Req<BOOK001Tranrq> requestBody) throws InsertFailException {

        log.info("-------- [BOOK-001] 建立訂單 API 啟動 --------");
        List<BOOKTranrqOrderDetail> orderDetails = requestBody.getTranrq().getOrderDetail();
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();
        BigDecimal orderPrice = new BigDecimal(0);

        synchronized (BOOKING_LOCK) {

            String itemsId = IdUtil.generateTableId("D", orderItemsRepository.findMaxId());

            for (BOOKTranrqOrderDetail orderDetail : orderDetails) {

                String roomId = orderDetail.getRoomId();
                String arrivalDate = orderDetail.getArrivalDate();
                Integer roomQuantity = orderDetail.getRoomQuantity();
                Integer roomPrice = orderDetail.getRoomPrice();

                Optional<RoomInventoriesEntity> orderedResult = roomInventoriesRepository.findByRoomIdAndStayDate(roomId, arrivalDate);

                if (orderedResult.isEmpty()) {
                    Optional<RoomsEntity> roomsEntity = roomsRepository.findById(roomId);
                    if (roomsEntity.isEmpty()) {
                        log.error("[BOOK-001] 查無房型編號 {}，訂單建立失敗", roomId);
                        throw new InsertFailException();
                    }
                    int availableQuantity = roomsEntity.get().getTotalUnits() - roomQuantity;
                    if (availableQuantity < 0) {
                        log.error("[BOOK-001] 由於該房型總數量不足，訂單建立失敗");
                        throw new InsertFailException();
                    }
                    roomInventoriesRepository.save(new RoomInventoriesEntity(IdUtil.generateTableId("Q", roomInventoriesRepository.findMaxId()), roomId, arrivalDate, availableQuantity, roomPrice));
                } else {
                    RoomInventoriesEntity inventory = orderedResult.get();
                    int originalAvailableQuantity = inventory.getAvailableQuantity();
                    if (roomQuantity > originalAvailableQuantity) {
                        log.error("[BOOK-001] 訂單要求該房型數量大於該房型總數量，訂單建立失敗");
                        throw new InsertFailException();
                    }
                    inventory.setAvailableQuantity(originalAvailableQuantity - roomQuantity);
                    roomInventoriesRepository.save(inventory);
                }

                OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
                orderItemsEntity.setId(itemsId);
                orderItemsEntity.setArrivalDate(arrivalDate);
                orderItemsEntity.setRoomId(roomId);
                orderItemsEntity.setQuantity(roomQuantity);
                orderItemsEntity.setPrice(roomPrice);
                orderItemsEntities.add(orderItemsEntity);

                orderPrice = orderPrice.add(BigDecimal.valueOf(roomPrice));
                itemsId = IdUtil.tableIdIncrement(itemsId);
            }

            BOOKTranrqOrderInfo orderInfo = requestBody.getTranrq().getOrderInfo();

            OrdersEntity ordersEntity = new OrdersEntity();
            ordersEntity.setId(IdUtil.generateTableId("O", ordersRepository.findMaxId()));
            ordersEntity.setUserId(usersRepository.findIdByAccountId(accountId));
            ordersEntity.setPropertyId(orderInfo.getPropertyId());
            ordersEntity.setPaymentId(orderInfo.getPaymentId());
            ordersEntity.setHotelCharges(orderPrice.intValue());
            ordersEntity.setCheckIn(orderInfo.getCheckIn());
            ordersEntity.setCheckOut(orderInfo.getCheckOut());
            ordersEntity.setStatus(orderInfo.getStatus());
            ordersEntity.setNote(orderInfo.getNote());
            ordersEntity.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

            if (!"y".equals(orderInfo.getGuest())) {
                ordersEntity.setGuest("n");
                ordersEntity.setGuestName(orderInfo.getGuestName());
                ordersEntity.setGuestPhone(orderInfo.getGuestPhone());
            } else {
                ordersEntity.setGuest("y");
            }

            String orderId;

            try {
                orderId = ordersRepository.save(ordersEntity).getId();
                orderItemsEntities.forEach((entity) -> entity.setOrderId(orderId));
                orderItemsRepository.saveAll(orderItemsEntities);
            } catch (Exception e) {
                log.error("[BOOK-001] 資料新增至資料庫發生異常，訂單建立失敗");
                throw new InsertFailException();
            }

            log.info("[BOOK-001] 建立訂單成功，訂單ID：{}", orderId);

//            // 發送訂單確認通知
//            if (accountId != null) {
//                sendNotification(accountId, "訂單成立", "您的訂單 " + orderId + " 已成功建立", "ORDER", orderId);
//            } else {
//                log.warn("[BOOK-001] accountId 為空，無法發送通知");
//            }

            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK001Tranrs(orderId));
        }
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
                        log.info("[BOOK-001] 通知發送成功，通知ID：{}", notificationId);
                    } catch (Exception e) {
                        log.error("[BOOK-001] 推播通知失敗，帳號ID：{}", accountId, e);
                    }
                }
            });
        } catch (JsonProcessingException e) {
            log.error("[BOOK-001] 發送通知失敗", e);
        }

    }

}