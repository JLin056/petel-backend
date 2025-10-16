package com.example.petel.service.impl;

import com.example.petel.dto.BOOK008Tranrq;
import com.example.petel.entity.TransactionsEntity;
import com.example.petel.exception.PaymentFailedException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.TransactionsRepository;
import com.example.petel.service.BOOK008Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * BOOK-008 綠界通知付款回應 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK008SvcImpl implements BOOK008Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000001";
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
    public String book008(BOOK008Tranrq requestBody) throws Exception {

        synchronized (PAYING_LOCK) {

            JSONObject jsonObject = new JSONObject(CodeUtil.dataDecrypt(requestBody.getData(), HASH_KEY, HASH_IV));
            JSONObject orderInfoJsonObject = new JSONObject(jsonObject.getString("OrderInfo"));

            TransactionsEntity transactionsEntity = new TransactionsEntity();
            transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
            transactionsEntity.setOrderId(orderInfoJsonObject.getString("MerchantTradeNo").replaceFirst("^PETEL", ""));
            transactionsEntity.setPaymentId(PAYMENT_ID);
            transactionsEntity.setTransactionId(orderInfoJsonObject.getString("TradeNo"));
            transactionsEntity.setFlowType("Pay");
            transactionsEntity.setHotelCharges(Integer.parseInt(orderInfoJsonObject.getString("TradeAmt")));
            transactionsEntity.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

            if (Integer.parseInt(jsonObject.getString("RtnCode")) != SUCCESS_RTN_CODE) {
                transactionsEntity.setStatus("付款失敗");
                transactionsRepository.save(transactionsEntity);
                log.error("[BOOK-008] 付款失敗");
                throw new PaymentFailedException();
            }

            transactionsEntity.setStatus("付款成功");
            transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
            if (orderInfoJsonObject.get("PaymentTypeChargeFee") == null) {
                transactionsEntity.setTransactionFee(0);
            } else {
                transactionsEntity.setTransactionFee(Integer.parseInt(orderInfoJsonObject.getString("PaymentTypeChargeFee")));
            }
            transactionsEntity.setPayTime(TimeUtil.parseMerchantTradeDate(orderInfoJsonObject.getString("TradeDate")));
            transactionsRepository.save(transactionsEntity);
        }
        return "1|OK";
    }
}
