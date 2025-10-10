package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrderItemsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.RoomEntity;
import com.example.petel.entity.RoomInventoriesEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrderItemsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.RoomInventoriesRepository;
import com.example.petel.repository.RoomRepository;
import com.example.petel.service.BOOK004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    /** RoomRepository */
    private final RoomRepository roomRepository;

    /**
     * 取消該筆訂單
     *
     * @param requestBody Req<BOOK004Tranrq>
     * @return Res<BOOKTranrs>
     * @throws DataNotFoundException 查無資料
     * @throws DeleteFailException   刪除失敗
     */
    @Override
    public Res<BOOKTranrs> book004(Req<BOOK004Tranrq> requestBody) throws DataNotFoundException, DeleteFailException {

        log.info("-------- [BOOK-004] 取消該筆訂單 API 啟動 --------");

        Long orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-004] 查無訂單編號為 {} 的資料，取消訂單失敗", orderId);
                    return new DataNotFoundException();
                });

        List<OrderItemsEntity> orderDetails = orderItemsRepository.findByOrderId(orderId);
        for (OrderItemsEntity orderDetail : orderDetails) {

            long roomId = orderDetail.getRoomId();

            RoomInventoriesEntity roomInventoriesEntity = roomInventoriesRepository.findByRoomIdAndStayDate(roomId, orderDetail.getArrivalDate()).orElseThrow(() -> {
                log.error("[BOOK-004] 查無庫存資料，取消訂單失敗");
                return new DataNotFoundException();
            });

            RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> {
                log.error("[BOOK-004] 查無房型編號 {} 資料，取消訂單失敗", roomId);
                return new DataNotFoundException();
            });

            int newAvailableQuantity = roomInventoriesEntity.getAvailableQuantity() + orderDetail.getQuantity();

            if (newAvailableQuantity > roomEntity.getTotalUnits()) {
                log.warn("[BOOK-004] 數據邏輯異常，請檢查");
                roomInventoriesEntity.setAvailableQuantity(roomEntity.getTotalUnits());
            } else {
                roomInventoriesEntity.setAvailableQuantity(newAvailableQuantity);
            }
            roomInventoriesRepository.save(roomInventoriesEntity);
        }

        try {
            orderItemsRepository.deleteByOrderId(orderId);
        } catch (Exception e) {
            log.error("[BOOK-004] 刪除異常，取消訂單失敗");
            throw new DeleteFailException();
        }

        ordersEntity.setStatus("取消訂單");
        ordersRepository.save(ordersEntity);

        log.info("[BOOK-004] 取消訂單成功");
        return new Res<BOOKTranrs>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOKTranrs(orderId));
    }
}