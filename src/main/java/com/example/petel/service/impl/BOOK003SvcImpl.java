package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BOOK-003 修改該筆訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK003SvcImpl implements BOOK003Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;

    /**
     * 修改該筆訂單
     *
     * @param requestBody Req<BOOK003Tranrq>
     * @return Res<BOOKTranrs>
     * @throws DataNotFoundException 查無資料
     * @throws UpdateFailException   修改失敗
     */
    @Override
    public Res<BOOKTranrs> book003(Req<BOOK003Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {

        log.info("-------- [BOOK-003] 修改該筆訂單 API 啟動 --------");

        Long orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity OrdersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-003] 查無訂單編號為 {} 的資料，修改訂單失敗", orderId);
                    return new DataNotFoundException();
                });

        try {
            OrdersEntity.setNote(requestBody.getTranrq().getNote());
            ordersRepository.save(OrdersEntity);
        } catch (Exception e) {
            log.error("[BOOK-003] 修改訂單失敗");
            throw new UpdateFailException();
        }

        log.info("[BOOK-003] 修改該筆訂單成功");
        return new Res<BOOKTranrs>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOKTranrs(orderId));
    }
}