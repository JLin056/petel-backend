package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.ADMIN005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ADMIN005SvcImpl implements ADMIN005Svc {

    private final OrdersRepository ordersRepository;

    /**
     * 取消訂單
     *
     * @param req Req<ADMIN005Tranrq>
     * @return Res<ADMIN005Tranrs>
     * @throws DataNotFoundException 訂單不存在
     * @throws UpdateFailException   取消失敗
     */
    @Override
    @Transactional
    public Res<ADMIN005Tranrs> cancelOrder(Req<ADMIN005Tranrq> req) throws DataNotFoundException, UpdateFailException {
        log.info("-------- [ADMIN-005] 取消訂單 ---------");
        ADMIN005Tranrq tranrq = req.getTranrq();
        String orderId = tranrq.getOrderId();

        log.info("[ADMIN-005] 取消訂單參數: orderId={}", orderId);

        // 查詢訂單是否存在
        OrdersEntity order = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("[ADMIN-005] 訂單不存在: orderId={}", orderId);
                    return new DataNotFoundException("訂單不存在");
                });

        // 檢查訂單是否已經被取消
        if ("取消訂單".equalsIgnoreCase(order.getStatus())) {
            log.warn("[ADMIN-005] 訂單已經是取消狀態: orderId={}", orderId);
            throw new UpdateFailException("訂單已經是取消狀態");
        }

        try {
            // 更新訂單狀態為 cancelled
            order.setStatus("取消訂單");
            order.setUpdatedAt(LocalDateTime.now());

            // 儲存更新
            OrdersEntity updatedOrder = ordersRepository.save(order);

            log.info("[ADMIN-005] 訂單取消成功: orderId={}", orderId);

            // 組裝回應
            ADMIN005Tranrs tranrs = new ADMIN005Tranrs();
            tranrs.setOrderId(updatedOrder.getId());
            tranrs.setStatus(updatedOrder.getStatus());
            tranrs.setMessage("訂單已成功取消");

            // 格式化更新時間為 yyyy-MM-dd
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            tranrs.setUpdatedAt(updatedOrder.getUpdatedAt().format(formatter));

            log.info("[ADMIN-005] 訂單取消完成: orderId={}", tranrs.getOrderId());

            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    tranrs
            );
        } catch (DataAccessException e) {
            log.error("[ADMIN-005] 取消訂單失敗: orderId={}, 錯誤訊息: {}", orderId, e.getMessage(), e);
            throw new UpdateFailException("取消訂單失敗: " + e.getMessage());
        }
    }
}
