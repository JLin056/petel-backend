package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PetelOrderItemsEntity;
import com.example.petel.entity.PetelOrdersEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PetelOrderItemsRepository;
import com.example.petel.repository.PetelOrdersRepository;
import com.example.petel.service.BOOK001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * BOOK-001 建立訂單 SvcImpl
 */
@Service
@RequiredArgsConstructor
public class BOOK001SvcImpl implements BOOK001Svc {

    /** PetelOrdersRepository */
    private final PetelOrdersRepository petelOrdersRepository;
    /** PetelOrderItemsRepository */
    private final PetelOrderItemsRepository petelOrderItemsRepository;

    /**
     * 建立訂單
     * @param requestBody Req<BOOK001Tranrq>
     * @return Res<BOOKTranrs>
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOKTranrs> book001(Req<BOOK001Tranrq> requestBody) throws Exception {

        try {

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

            PetelOrdersEntity savedEntity = petelOrdersRepository.save(petelOrdersEntity);

            List<BOOKTranrqOrderDetail> orderDetail = requestBody.getTranrq().getOrderDetail();

            for (int i = 0; i < orderDetail.size(); i++) {
                PetelOrderItemsEntity petelOrderItemsEntity = new PetelOrderItemsEntity();
                petelOrderItemsEntity.setOrderId(savedEntity.getId());
                petelOrderItemsEntity.setArrivalDate(orderDetail.get(i).getArrivalDate());
                petelOrderItemsEntity.setProductId(orderDetail.get(i).getProductId());
                petelOrderItemsEntity.setQuantity(orderDetail.get(i).getProductQuantity());
                petelOrderItemsEntity.setPrice(orderDetail.get(i).getProductPrice());
                petelOrderItemsRepository.save(petelOrderItemsEntity);
            }

            Res<BOOKTranrs> responseBody = new Res<>();
            responseBody.setMwHeader(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS));
            responseBody.setTranrs(new BOOKTranrs(savedEntity.getId()));
            return responseBody;

        } catch (Exception e) {
            throw new InsertFailException();
        }
    }
}