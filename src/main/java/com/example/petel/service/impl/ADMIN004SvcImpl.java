package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.ADMIN004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ADMIN004SvcImpl implements ADMIN004Svc {

    private final OrdersRepository ordersRepository;

    /**
     * 更新訂單備註
     *
     * @param req Req<ADMIN004Tranrq>
     * @return Res<ADMIN004Tranrs>
     * @throws DataNotFoundException 訂單不存在
     * @throws UpdateFailException   更新失敗
     */
    @Override
    @Transactional
    public Res<ADMIN004Tranrs> updateOrderNote(Req<ADMIN004Tranrq> req) throws DataNotFoundException, UpdateFailException, IOException {
        log.info("-------- [ADMIN-004] 更新訂單備註 ---------");
        ADMIN004Tranrq tranrq = req.getTranrq();
        String orderId = tranrq.getOrderId();
        String note = tranrq.getNote() != null ? tranrq.getNote() : "";

        log.info("[ADMIN-004] 更新參數: orderId={}, note={}", orderId, note);

        // 查詢訂單是否存在
        OrdersEntity order = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("[ADMIN-004] 訂單不存在: orderId={}", orderId);
                    return new DataNotFoundException("訂單不存在");
                });

        // 更新備註和更新時間
        order.setNote(note);
        order.setUpdatedAt(LocalDateTime.now());

        // 儲存更新
        OrdersEntity updatedOrder = ordersRepository.save(order);

        log.info("[ADMIN-004] 訂單備註更新成功: orderId={}", orderId);

        // 組裝回應
        ADMIN004Tranrs tranrs = new ADMIN004Tranrs();
        tranrs.setOrderId(updatedOrder.getId());
        tranrs.setNote(updatedOrder.getNote());

        // 格式化更新時間為 YYYY-MM-DD
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        tranrs.setUpdatedAt(updatedOrder.getUpdatedAt().format(formatter));

        log.info("[ADMIN-004] 訂單備註更新完成: orderId={}", tranrs.getOrderId());

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
