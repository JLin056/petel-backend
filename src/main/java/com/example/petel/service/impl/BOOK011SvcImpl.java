package com.example.petel.service.impl;

import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.PaymentFailedException;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.service.BOOK011Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * BOOK-011 綠界通知付款回應 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK011SvcImpl implements BOOK011Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** PAYMENT_ID */
    private final long PAYMENT_ID = 2L;
    /** SUCCESS_RTN_CODE */
    private final int SUCCESS_RTN_CODE = 1;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;

    @Override
    public String book011(Map<String, Object> requestParam) throws Exception {

        long orderId = Long.parseLong(requestParam.get("MerchantTradeNo").toString().replaceFirst("^PETEL0+", ""));

        TransactionsEntity transactionsEntity = new TransactionsEntity();
        transactionsEntity.setOrderId(orderId);
        transactionsEntity.setPaymentId(PAYMENT_ID);
        transactionsEntity.setTxnId(requestParam.get("TradeNo").toString());
        transactionsEntity.setFlowType("Pay");
        transactionsEntity.setHotelCharges((Integer) requestParam.get("TradeAmt"));
        transactionsEntity.setCreatedAt(Timestamp.from(Instant.now()));

        if ((Integer) requestParam.get("RtnCode") != SUCCESS_RTN_CODE) {
            transactionsEntity.setStatus("付款失敗");
            log.error("[BOOK-011] 付款失敗");
            throw new PaymentFailedException();
        }

        if (!CodeUtil.generateCheckMacValue(requestParam, HASH_KEY, HASH_IV).equals(requestParam.get("CheckMacValue").toString())) {
            transactionsEntity.setStatus("付款異常");
            log.error("[BOOK-011] 檢查碼相異");
            throw new PaymentFailedException();
        }

        transactionsEntity.setStatus("付款成功");
        transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
        transactionsEntity.setTransactionFee((Integer) requestParam.get("PaymentTypeChargeFee"));
        transactionsEntity.setPayTime(TimeUtil.parseMerchantTradeDate(requestParam.get("MerchantTradeDate").toString()));

        return "1|OK";
    }
}