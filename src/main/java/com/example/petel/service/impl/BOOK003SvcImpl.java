package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PetelOrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PetelOrdersRepository;
import com.example.petel.service.BOOK003Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BOOK-003 修改該筆訂單 SvcImpl
 */
@Service
@RequiredArgsConstructor
public class BOOK003SvcImpl implements BOOK003Svc {

    /** PetelOrdersRepository */
    private final PetelOrdersRepository petelOrdersRepository;

    /**
     * 修改該筆訂單
     * @param requestBody Req<BOOK003Tranrq>
     * @return Res<BOOKTranrs>
     */
    @Override
    public Res<BOOKTranrs> book003(Req<BOOK003Tranrq> requestBody) throws Exception {

        Long orderId = requestBody.getTranrq().getOrderId();

        PetelOrdersEntity petelOrdersEntity = petelOrdersRepository.findById(orderId)
                .orElseThrow(DataNotFoundException::new);

        try {
            petelOrdersEntity.setNote(requestBody.getTranrq().getNote());
            petelOrdersRepository.save(petelOrdersEntity);
        } catch (Exception e) {
            throw new UpdateFailException();
        }

        Res<BOOKTranrs> responseBody = new Res<>();
        responseBody.setMwHeader(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS));
        responseBody.setTranrs(new BOOKTranrs(orderId));
        return responseBody;
    }
}