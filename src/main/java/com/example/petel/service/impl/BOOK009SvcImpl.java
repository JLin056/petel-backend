package com.example.petel.service.impl;

import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.PaymentFailedException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.service.BOOK009Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

/**
 * BOOK-009 綠界通知付款回應 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK009SvcImpl implements BOOK009Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000002";
    /** SUCCESS_RTN_CODE */
    private static final int SUCCESS_RTN_CODE = 1;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;
    /** PAYING_LOCK */
    private static final Object PAYING_LOCK = new Object();

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String book009(Map<String, Object> requestParam) throws Exception {

        synchronized (PAYING_LOCK) {

            TransactionsEntity transactionsEntity = new TransactionsEntity();
            transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
            transactionsEntity.setOrderId(requestParam.get("MerchantTradeNo").toString().replaceFirst("^PETEL", ""));
            transactionsEntity.setPaymentId(PAYMENT_ID);
            transactionsEntity.setTransactionId(requestParam.get("TradeNo").toString());
            transactionsEntity.setFlowType("Pay");
            transactionsEntity.setHotelCharges(Integer.parseInt(requestParam.get("TradeAmt").toString()));
            transactionsEntity.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

            if (Integer.parseInt(requestParam.get("RtnCode").toString()) != SUCCESS_RTN_CODE) {
                transactionsEntity.setStatus("付款失敗");
                transactionsRepository.save(transactionsEntity);
                log.warn("[BOOK-009] 付款失敗");
                return "1|OK";
            }

            if (!CodeUtil.generateCheckMacValue(requestParam, HASH_KEY, HASH_IV).equals((requestParam.get("CheckMacValue").toString()))) {
                transactionsEntity.setStatus("付款異常");
                transactionsRepository.save(transactionsEntity);
                log.warn("[BOOK-009] 檢查碼相異");
                return "1|OK";
            }

            transactionsEntity.setStatus("付款成功");
            transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
            if (requestParam.get("PaymentTypeChargeFee") == null) {
                transactionsEntity.setTransactionFee(0);
            } else {
                transactionsEntity.setTransactionFee(Integer.parseInt(requestParam.get("PaymentTypeChargeFee").toString()));
            }
            transactionsEntity.setPayTime(TimeUtil.parseMerchantTradeDate(requestParam.get("TradeDate").toString()));
            transactionsRepository.save(transactionsEntity);
        }

        return "1|OK";
    }
}