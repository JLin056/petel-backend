package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.MERCH014Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH014SvcImpl implements MERCH014Svc {

    /**
     * OrdersRepository
     */
    private final OrdersRepository ordersRepository;

    /**
     * 更新訂單狀態
     *
     * @param requestBody Req<MERCH014Tranrq> (包含 orderId + newStatus)
     * @return Res<MERCH014Tranrs>
     * @throws DataNotFoundException, UpdateFailException
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public Res<MERCH014Tranrs> updateStatus(Req<MERCH014Tranrq> requestBody)
            throws DataNotFoundException, UpdateFailException {

        log.info("-------- [MERCH-014] 更新訂單狀態 ---------");

        MERCH014Tranrq merch014Tranrq = requestBody.getTranrq();
        String orderId = merch014Tranrq.getId();
        String newStatus = merch014Tranrq.getStatus();

        log.info("[MERCH-014] 查詢 orderId = {}", orderId);

        Optional<OrdersEntity> optional = ordersRepository.findById(orderId);
        if (optional.isEmpty()) {
            log.warn("[MERCH-014] 查無此訂單，orderId = {}", orderId);
            throw new DataNotFoundException("查無該筆訂單資料");
        }

        OrdersEntity existingOrder = optional.get();

        try {
            existingOrder.setStatus(newStatus);
            ordersRepository.save(existingOrder);

            log.info("[MERCH-014] 更新成功，orderId={}，newStatus={}", orderId, newStatus);

        } catch (Exception e) {
            log.error("[MERCH-014] 更新訂單狀態失敗", e);
            throw new UpdateFailException("更新訂單狀態失敗");
        }

        MERCH014Tranrs merch014Tranrs = new MERCH014Tranrs();
        merch014Tranrs.setOrderId(orderId);
        merch014Tranrs.setStatus(newStatus);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch014Tranrs
        );
    }
}
