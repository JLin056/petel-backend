package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PetelOrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PetelOrderItemsRepository;
import com.example.petel.repository.PetelOrdersRepository;
import com.example.petel.service.BOOK004Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BOOK-004 取消該筆訂單 SvcImpl
 */
@Service
@RequiredArgsConstructor
public class BOOK004SvcImpl implements BOOK004Svc {

    /** PetelOrdersRepository */
    private final PetelOrdersRepository petelOrdersRepository;
    /** PetelOrderItemsRepository */
    private final PetelOrderItemsRepository petelOrderItemsRepository;

    /**
     * 取消該筆訂單
     * @param requestBody Req<BOOK004Tranrq>
     * @return Res<BOOKTranrs>
     */
    @Override
    public Res<BOOKTranrs> book004(Req<BOOK004Tranrq> requestBody) throws Exception {

        Long orderId = requestBody.getTranrq().getOrderId();

        PetelOrdersEntity petelOrdersEntity = petelOrdersRepository.findById(orderId)
                .orElseThrow(DataNotFoundException::new);
        petelOrdersEntity.setStatus("取消訂單");
        petelOrdersRepository.save(petelOrdersEntity);

        try {
            petelOrderItemsRepository.deleteByOrderId(orderId);
        } catch (Exception e) {
            throw new DeleteFailException();
        }

        Res<BOOKTranrs> responseBody = new Res<>();
        responseBody.setMwHeader(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS));
        responseBody.setTranrs(new BOOKTranrs(orderId));
        return responseBody;
    }
}