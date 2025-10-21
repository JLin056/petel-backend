package com.example.petel.service.impl;

import com.example.petel.dto.BOOK003Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK003Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

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
     * @return Res<Object>
     * @throws DataNotFoundException 查無資料
     * @throws UpdateFailException   修改失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<Object> book003(Req<BOOK003Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {

        log.info("-------- [BOOK-003] 修改該筆訂單 API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-003] 查無訂單編號為 {} 的資料，修改訂單失敗", orderId);
                    return new DataNotFoundException();
                });

        try {
            ordersEntity.setNote(requestBody.getTranrq().getNote());
            ordersEntity.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
            ordersRepository.save(ordersEntity);
        } catch (Exception e) {
            log.error("[BOOK-003] 更新過程有問題，修改訂單編號為 {} 的資料失敗", orderId);
            throw new UpdateFailException();
        }

        log.info("[BOOK-003] 修改訂單編號為 {} 的資料成功", orderId);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }
}