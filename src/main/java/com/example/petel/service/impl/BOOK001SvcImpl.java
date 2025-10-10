package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrderItemsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.RoomEntity;
import com.example.petel.entity.RoomInventoriesEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrderItemsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.RoomInventoriesRepository;
import com.example.petel.repository.RoomRepository;
import com.example.petel.service.BOOK001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
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
    /** RoomRepository */
    private final RoomRepository roomRepository;

    /**
     * 建立訂單
     *
     * @param requestBody Req<BOOK001Tranrq>
     * @return Res<BOOKTranrs>
     * @throws InsertFailException 新增失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOKTranrs> book001(Req<BOOK001Tranrq> requestBody) throws InsertFailException {

        log.info("-------- [BOOK-001] 建立訂單 API 啟動 --------");
        List<BOOKTranrqOrderDetail> orderDetails = requestBody.getTranrq().getOrderDetail();
        List<OrderItemsEntity> orderItemsEntities = new ArrayList<>();

        for (BOOKTranrqOrderDetail orderDetail : orderDetails) {

            long roomId = orderDetail.getRoomId();
            String arrivalDate = orderDetail.getArrivalDate();
            int roomQuantity = orderDetail.getRoomQuantity();
            int roomPrice = orderDetail.getRoomPrice();

            Optional<RoomInventoriesEntity> orderedResult = roomInventoriesRepository.findByRoomIdAndStayDate(roomId, arrivalDate);

            if (orderedResult.isEmpty()) {
                Optional<RoomEntity> roomEntity = roomRepository.findByRoomId(roomId);
                if (roomEntity.isEmpty()) {
                    log.error("[BOOK-001] 查無房型編號 {}，訂單建立失敗", roomId);
                    throw new InsertFailException();
                }
                int availableQuantity = roomEntity.get().getTotalUnits() - roomQuantity;
                if (availableQuantity < 0) {
                    log.error("[BOOK-001] 由於該房型總數量不足，訂單建立失敗");
                    throw new InsertFailException();
                }
                roomInventoriesRepository.save(new RoomInventoriesEntity(roomId, arrivalDate, availableQuantity, roomPrice));
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
            orderItemsEntity.setArrivalDate(arrivalDate);
            orderItemsEntity.setRoomId(roomId);
            orderItemsEntity.setQuantity(roomQuantity);
            orderItemsEntity.setPrice(roomPrice);
            orderItemsEntities.add(orderItemsEntity);
        }

        BOOKTranrqOrderInfo orderInfo = requestBody.getTranrq().getOrderInfo();

        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setUserId(orderInfo.getUserId());
        ordersEntity.setPropertyId(orderInfo.getPropertyId());
        ordersEntity.setPaymentId(orderInfo.getPaymentId());
        ordersEntity.setHotelCharges(orderInfo.getHotelCharges());
        ordersEntity.setCheckIn(orderInfo.getCheckIn());
        ordersEntity.setCheckOut(orderInfo.getCheckOut());
        ordersEntity.setStatus(orderInfo.getStatus());
        ordersEntity.setNote(orderInfo.getNote());
        ordersEntity.setCreatedAt(Timestamp.from(Instant.now()));

        OrdersEntity savedEntity;

        try {
            savedEntity = ordersRepository.save(ordersEntity);
            orderItemsEntities.forEach((entity) -> entity.setOrderId(savedEntity.getId()));
            orderItemsRepository.saveAll(orderItemsEntities);
        } catch (Exception e) {
            log.error("[BOOK-001] 資料新增至資料庫發生異常，訂單建立失敗");
            throw new InsertFailException();
        }

        log.info("[BOOK-001] 建立訂單成功");
        return new Res<BOOKTranrs>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOKTranrs(savedEntity.getId()));
    }
}