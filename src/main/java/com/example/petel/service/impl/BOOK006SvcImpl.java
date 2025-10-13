package com.example.petel.service.impl;

import com.example.petel.dto.BOOK006Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.RefundFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.TimeUtil;
import com.example.petel.repository.*;
import com.example.petel.service.BOOK006Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
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
    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** MIN_DAYS_FULL_REFUND */
    private static final int MIN_DAYS_FULL_REFUND = 10;
    /** MIN_DAYS_HALF_REFUND */
    private static final int MIN_DAYS_HALF_REFUND = 5;
    /** ATM_REFUND_FEE */
    private static final int ATM_REFUND_FEE = 15;
    /** PAYMENT_ID_ATM */
    private static final int PAYMENT_ID_ATM = 3;

    /**
     * 退款
     *
     * @param requestBody Req<BOOK006Tranrq>
     * @return Res<Object>
     */
    @Override
    public Res<Object> book006(Req<BOOK006Tranrq> requestBody) throws DataNotFoundException, RefundFailException {

        long orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-006] 查無訂單編號為 {} 的訂單資料，請求退款失敗", orderId);
            return new DataNotFoundException();
        });

        if (!("已付款".equals(ordersEntity.getStatus()))) {
            log.error("[BOOK-006] 訂單編號為 {} 的訂單資料並非已付款狀態，請求退款失敗", orderId);
            throw new RefundFailException();
        }

        TransactionsEntity transactionsEntity = transactionsRepository.findByOrderId(orderId).orElseThrow(() -> {
            log.error("[BOOK-006] 查無訂單編號為 {} 的交易資料，請求退款失敗", orderId);
            return new DataNotFoundException();
        });

        if ("付款失敗".equals(transactionsEntity.getStatus())) {
            log.error("[BOOK-006] 交易編號為 {} 的交易資料狀態為付款失敗，無法請求退款", orderId);
            throw new RefundFailException();
        }

        long differenceOfDays = TimeUtil.getDifferenceOfDays(LocalDate.now().toString(), ordersEntity.getCheckIn());
        double ratio;

        if (differenceOfDays >= MIN_DAYS_FULL_REFUND) {
            ratio = 1;
        } else if (differenceOfDays >= MIN_DAYS_HALF_REFUND) {
            ratio = 0.5;
        } else {
            log.info("[BOOK-006] 請注意，退款比例為 0");
            ratio = 0;
        }

        BigDecimal transactionPrice = new BigDecimal(transactionsEntity.getHotelCharges());
        BigDecimal refundRatio = BigDecimal.valueOf(ratio);
        transactionsEntity.setFlowType("refund");

        int refundAmount = transactionPrice.multiply(refundRatio).setScale(0, RoundingMode.FLOOR).intValue();

        if (transactionsEntity.getPaymentId() == PAYMENT_ID_ATM) {
            if (refundAmount >= 15) {
                log.info("[BOOK-006] 使用虛擬 ATM 帳號且退款金額 >= 15：請注意，綠界不支援信用卡以外的退款，在這裡使用預設的ATM退款手續費");
                transactionsEntity.setTransactionFee(BigDecimal.valueOf(transactionsEntity.getTransactionFee()).add(BigDecimal.valueOf(ATM_REFUND_FEE)).intValue());
                log.info("[BOOK-006] 使用虛擬 ATM 帳號且退款金額 >= 15：退款金額 = 交易金額 * 退款比例 - 交易手續費，有小數點無條件捨去至整數位");
                transactionsEntity.setRefundAmount(BigDecimal.valueOf(refundAmount).subtract(BigDecimal.valueOf(ATM_REFUND_FEE)).intValue());
            } else {
                log.info("[BOOK-006] 使用虛擬 ATM 帳號且退款金額 < 15：不退款也不收退款手續費");
                transactionsEntity.setRefundAmount(0);
            }
        } else {
            log.info("[BOOK-006] 退款金額 = 交易金額 * 退款比例，有小數點無條件捨去至整數位");
            transactionsEntity.setRefundAmount(refundAmount);
        }

        transactionsEntity.setUpdatedAt(Timestamp.from(Instant.now()));
        transactionsRepository.save(transactionsEntity);

        log.info("[BOOK-006] 訂單編號為 {} 的訂單，請求退款成功", orderId);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new HashMap<>());
    }
}