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



            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK001Tranrs(orderId));
        }
    }


}