package com.example.petel.service.impl;

import com.example.petel.entity.OrderItemsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.RoomInventoriesEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.repository.OrderItemsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.RoomInventoriesRepository;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.BOOK012Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * BOOK-012 未付款定時取消訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK012SvcImpl implements BOOK012Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** OrderItemsRepository */
    private final OrderItemsRepository orderItemsRepository;
    /** RoomInventoriesRepository */
    private final RoomInventoriesRepository roomInventoriesRepository;
    /** RoomsRepository */
    private final RoomsRepository roomsRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void book012() throws Exception {

        log.info("-------- [BOOK-012] 未付款定時取消訂單 API 啟動 --------");

        for (OrdersEntity pendingOrder : ordersRepository.findByStatusAndCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).minusMinutes(10))) {

            String orderId = pendingOrder.getId();

            for (OrderItemsEntity pendingOrderItem : orderItemsRepository.findByOrderId(orderId)) {

                String roomId = pendingOrderItem.getRoomId();

                RoomsEntity roomsEntity = roomsRepository.findById(roomId).orElseThrow(() -> {
                    log.error("[BOOK-012] 查無房型編號 {} 資料，取消訂單失敗", roomId);
                    return new DataNotFoundException();
                });

                RoomInventoriesEntity roomInventoriesEntity = roomInventoriesRepository.findByRoomIdAndStayDate(roomId, pendingOrderItem.getArrivalDate()).orElseThrow(() -> {
                    log.error("[BOOK-012] 查無庫存資料，取消訂單失敗");
                    return new DataNotFoundException();
                });

                int newAvailableQuantity = roomInventoriesEntity.getAvailableQuantity() + pendingOrderItem.getQuantity();

                if (newAvailableQuantity > roomsEntity.getTotalUnits()) {
                    log.warn("[BOOK-012] 數據邏輯異常，請檢查");
                    roomInventoriesEntity.setAvailableQuantity(roomsEntity.getTotalUnits());
                } else {
                    roomInventoriesEntity.setAvailableQuantity(newAvailableQuantity);
                }
                roomInventoriesRepository.save(roomInventoriesEntity);
            }

            try {
                orderItemsRepository.deleteByOrderId(orderId); // 先刪子表
                ordersRepository.delete(pendingOrder); // 再刪父表
            } catch (Exception e) {
                log.error("[BOOK-012] 刪除異常");
                throw new DeleteFailException();
            }
        }
        log.info("-------- [BOOK-012] 未付款定時取消訂單 API 執行完成 --------");
    }
}
