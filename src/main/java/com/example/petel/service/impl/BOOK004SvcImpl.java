package com.example.petel.service.impl;

import com.example.petel.dto.BOOK004Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrderItemsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.RoomInventoriesEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrderItemsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.RoomInventoriesRepository;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.BOOK004Svc;
import jakarta.transaction.Transactional;
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

        log.info("[BOOK-004] 訂單編號為 {} 的資料，取消訂單成功", orderId);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }
}