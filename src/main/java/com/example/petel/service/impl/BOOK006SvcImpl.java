package com.example.petel.service.impl;

import com.example.petel.dto.BOOK006Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.RefundFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.*;
import com.example.petel.service.BOOK006Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

/**
 * BOOK-006 退款 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK006SvcImpl implements BOOK006Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** OrderItemsRepository */
    private final OrderItemsRepository orderItemsRepository;
    /** RoomInventoriesRepository */
    private final RoomInventoriesRepository roomInventoriesRepository;
    /** RoomRepository */
    private final RoomRepository roomRepository;
    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** VirtualAccountsRepository */
    private final VirtualAccountsRepository virtualAccountsRepository;

    /**
     * 退款
     *
     * @param requestBody Req<BOOK006Tranrq>
     * @return Res<Object>
     */
    @Override
    public Res<Object> book006(Req<BOOK006Tranrq> requestBody) throws Exception {

        long orderId = requestBody.getTranrq().getOrderId();

        TransactionsEntity transactionsEntity = transactionsRepository.findByOrderId(orderId).orElseThrow(() -> {
            log.error("[BOOK-006] 查無訂單編號為 {} 的交易資料，請求退款失敗", orderId);
            return new DataNotFoundException();
        });

        transactionsEntity.setFlowType("refund");

        if ("付款失敗".equals(transactionsEntity.getStatus())) {
            throw new RefundFailException();
        }

        BigDecimal transactionPrice = new BigDecimal(transactionsEntity.getHotelCharges());
        BigDecimal refundRatio = new BigDecimal(requestBody.getTranrq().getRefundRatio());
        transactionsEntity.setRefundAmount(transactionPrice.multiply(refundRatio).intValue());
        transactionsEntity.setUpdatedAt(Timestamp.from(Instant.now()));

        // TODO 利用時間差取得退款比例，不須放在上行

        log.info("[BOOK-006] 請求退款成功");
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }
}