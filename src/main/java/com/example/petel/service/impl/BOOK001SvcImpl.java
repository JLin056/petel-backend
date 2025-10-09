package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PetelOrderItemsEntity;
import com.example.petel.entity.PetelOrdersEntity;
import com.example.petel.entity.PetelRoomInventoriesEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PetelOrderItemsRepository;
import com.example.petel.repository.PetelOrdersRepository;
import com.example.petel.repository.PetelRoomInventoriesRepository;
import com.example.petel.service.BOOK001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * BOOK-001 建立訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK001SvcImpl implements BOOK001Svc {

    /**
     * PetelOrdersRepository
     */
    private final PetelOrdersRepository petelOrdersRepository;
    /**
     * PetelOrderItemsRepository
     */
    private final PetelOrderItemsRepository petelOrderItemsRepository;
    /**
     * PetelRoomInventoriesRepository
     */
    private final PetelRoomInventoriesRepository petelRoomInventoriesRepository;

    /**
     * 建立訂單
     *
     * @param requestBody Req<BOOK001Tranrq>
     * @return Res<BOOKTranrs>
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOKTranrs> book001(Req<BOOK001Tranrq> requestBody) throws Exception {

        log.info("-------- [BOOK-001] 建立訂單 --------");

        BOOKTranrqOrderInfo orderInfo = requestBody.getTranrq().getOrderInfo();

        PetelOrdersEntity petelOrdersEntity = new PetelOrdersEntity();
        petelOrdersEntity.setUserId(orderInfo.getUserId());
        petelOrdersEntity.setPropertyId(orderInfo.getPropertyId());
        petelOrdersEntity.setPaymentId(orderInfo.getPaymentId());
        petelOrdersEntity.setHotelCharges(orderInfo.getHotelCharges());
        petelOrdersEntity.setCheckIn(orderInfo.getCheckIn());
        petelOrdersEntity.setCheckOut(orderInfo.getCheckOut());
        petelOrdersEntity.setStatus(orderInfo.getStatus());
        petelOrdersEntity.setNote(orderInfo.getNote());
        petelOrdersEntity.setCreatedAt(Timestamp.from(Instant.now()));

        PetelOrdersEntity savedEntity;

        try {
            savedEntity = petelOrdersRepository.save(petelOrdersEntity);
        } catch (Exception e) {
            log.error("[BOOK-001] 建立訂單失敗 on PETEL_ORDERS");
            throw new InsertFailException();
        }

        List<BOOKTranrqOrderDetail> orderDetails = requestBody.getTranrq().getOrderDetail();

        List<PetelOrderItemsEntity> petelOrderItemsEntities = new ArrayList<>();
        List<PetelRoomInventoriesEntity> petelRoomInventoriesEntities = new ArrayList<>();

        for (int i = 0; i < orderDetails.size(); i++) {
            BOOKTranrqOrderDetail orderDetail = orderDetails.get(i);
            PetelOrderItemsEntity petelOrderItemsEntity = new PetelOrderItemsEntity();
            petelOrderItemsEntity.setOrderId(savedEntity.getId());
            petelOrderItemsEntity.setArrivalDate(orderDetail.getArrivalDate());
            petelOrderItemsEntity.setProductId(orderDetail.getProductId());
            petelOrderItemsEntity.setQuantity(orderDetail.getProductQuantity());
            petelOrderItemsEntity.setPrice(orderDetail.getProductPrice());
            petelOrderItemsEntities.add(petelOrderItemsEntity);

            List<PetelRoomInventoriesEntity> petelRoomInventoriesEntity = petelRoomInventoriesRepository.findByProductIdAndStayDate(orderDetail.getProductId(), orderDetail.getArrivalDate());

            if (petelRoomInventoriesEntity.isEmpty()) {
                log.error("[BOOK-001] 查無房庫 productId={} arrivalDate='{}'", orderDetail.getProductId(), orderDetail.getArrivalDate());
                throw new InsertFailException();
            }

            PetelRoomInventoriesEntity firstPetelRoomInventoriesEntity = petelRoomInventoriesEntity.get(0);
            firstPetelRoomInventoriesEntity.setAvailableQuantity(firstPetelRoomInventoriesEntity.getAvailableQuantity() - orderDetail.getProductQuantity());
            petelRoomInventoriesEntities.add(firstPetelRoomInventoriesEntity);
        }

        try {
            petelOrderItemsRepository.saveAll(petelOrderItemsEntities);
        } catch (Exception e) {
            log.error("[BOOK-001] 建立訂單失敗 on PETEL_ORDER_ITEMS");
            throw new InsertFailException();
        }

        try {
            petelRoomInventoriesRepository.saveAll(petelRoomInventoriesEntities);
        } catch (Exception e) {
            log.error("[BOOK-001] 建立訂單失敗 on PETEL_ROOM_INVENTORIES");
            throw new InsertFailException();
        }

        log.info("[BOOK-001] 建立訂單成功");

        Res<BOOKTranrs> responseBody = new Res<>();
        responseBody.setMwHeader(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS));
        responseBody.setTranrs(new BOOKTranrs(savedEntity.getId()));
        return responseBody;
    }
}