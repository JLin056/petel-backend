package com.example.petel.service.impl;

import com.example.petel.dto.BOOK013Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.service.BOOK013Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

/**
 * BOOK-013 模擬更新訂單付款狀態 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK013SvcImpl implements BOOK013Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000002";

    /**
     * 模擬更新訂單付款狀態 (線上刷卡)
     *
     * @param requestBody Req<BOOK013Tranrq>
     * @return Res<Object>
     * @throws DataNotFoundException 查無資料
     * @throws UpdateFailException 修改失敗
     * @throws InvalidPaymentMethodException 付款方式異常
     * @throws InvalidPaymentMethodException 新增失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<Object> book013(Req<BOOK013Tranrq> requestBody) throws DataNotFoundException, UpdateFailException, InvalidPaymentMethodException, InsertFailException {

        log.info("-------- [BOOK-013] 模擬更新訂單付款狀態 (線上刷卡) API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("[BOOK-013] 查無訂單編號為 {} 的資料", orderId);
                    return new DataNotFoundException();
                });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.warn("[BOOK-013] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        ordersEntity.setStatus("已付款");
        ordersEntity.setUpdatedAt(now);

        TransactionsEntity transactionsEntity = new TransactionsEntity();

        transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
        transactionsEntity.setOrderId(orderId);
        transactionsEntity.setPaymentId(PAYMENT_ID);
        transactionsEntity.setTransactionId("PETEL" + orderId);
        transactionsEntity.setFlowType("Pay");
        transactionsEntity.setHotelCharges(ordersEntity.getHotelCharges());
        transactionsEntity.setCreatedAt(now);
        transactionsEntity.setStatus("付款成功");
        transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
        transactionsEntity.setTransactionFee(0);
        transactionsEntity.setPayTime(now);

        try {
            ordersRepository.save(ordersEntity);
        } catch (Exception e) {
            log.error("[BOOK-013] 更新過程有問題，修改訂單編號為 {} 的訂單資料失敗", orderId);
            throw new UpdateFailException();
        }

        try {
            transactionsRepository.save(transactionsEntity);
        } catch (Exception e) {
            log.error("[BOOK-013] 新增資料有問題，新增訂單編號為 {} 的交易資料失敗", orderId);
            throw new InsertFailException();
        }

        log.info("[BOOK-013] 模擬更新訂單付款狀態 (線上刷卡) API 執行完成");

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }
}