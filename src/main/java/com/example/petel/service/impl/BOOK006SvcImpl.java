package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK006Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * BOOK-006 組合付款參數 (線上刷卡) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK006SvcImpl implements BOOK006Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000002";
    /** MERCHANT_ID */
    @Value("${ecpay.merchantId}")
    private String MERCHANT_ID;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;
    /** RETURN_URL */
    @Value("${ecpay.credit.returnUrl}")
    private String RETURN_URL;
    /** CLIENT_BACK_URL */
    @Value("${ecpay.credit.clientBackUrl}")
    private String CLIENT_BACK_URL;
    /** ITEM_NAME */
    private static final String ITEM_NAME = "PETEL room(s)";

    @Override
    public Res<BOOK006Tranrs> book006(Req<BOOK006Tranrq> requestBody) throws Exception {

        log.info("-------- [BOOK-006] 組合付款參數 (線上刷卡) API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-006] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
            return new DataNotFoundException();
        });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.error("[BOOK-006] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

        Map<String, Object> ecPayParams = new HashMap<>();
        ecPayParams.put("MerchantID", MERCHANT_ID);
        ecPayParams.put("MerchantTradeNo", "PETEL" + orderId);
        ecPayParams.put("MerchantTradeDate", TimeUtil.getMerchantTradeDate(ordersEntity.getCreatedAt()));
        ecPayParams.put("PaymentType", "aio");
        ecPayParams.put("TotalAmount", ordersEntity.getHotelCharges());
        ecPayParams.put("TradeDesc", "建立訂單");
        ecPayParams.put("ItemName", ITEM_NAME);
        ecPayParams.put("ReturnURL", RETURN_URL);
        ecPayParams.put("ChoosePayment", "Credit");
        ecPayParams.put("EncryptType", 1);
        ecPayParams.put("ClientBackURL", CLIENT_BACK_URL);
        ecPayParams.put("CheckMacValue", CodeUtil.generateCheckMacValue(ecPayParams, HASH_KEY, HASH_IV));

        log.info("[BOOK-006] 訂單編號為 {} 的訂單，付款參數組合完成", orderId);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK006Tranrs(ecPayParams));
    }
}
