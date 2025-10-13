package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK009Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * BOOK-009 組合付款參數 (轉帳) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK009SvcImpl implements BOOK009Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** MERCHANT_ID */
    @Value("${ecpay.merchantId}")
    private String MERCHANT_ID;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;
    /** RETURN_URL */ // TODO 修改成外界可以進入的URL
    private static final String RETURN_URL = "http://localhost:8080/bookings/atm/notify";
    /** ITEM_NAME */
    private static final String ITEM_NAME = "PETEL room(s)";

    @Override
    public Res<BOOK009Tranrs> book009(Req<BOOK009Tranrq> requestBody) throws Exception {

        long orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-009] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
            return new DataNotFoundException();
        });

        if (ordersEntity.getPaymentId() != 3) {
            log.error("[BOOK-009] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

        Map<String, Object> ecPayParams = new HashMap<>();
        ecPayParams.put("MerchantID", MERCHANT_ID);
        ecPayParams.put("MerchantTradeNo", "PETEL" + String.format("%015d", orderId));
        ecPayParams.put("MerchantTradeDate", TimeUtil.getMerchantTradeDate(ordersEntity.getCreatedAt()));
        ecPayParams.put("PaymentType", "aio");
        ecPayParams.put("TotalAmount", ordersEntity.getHotelCharges());
        ecPayParams.put("TradeDesc", "建立訂單");
        ecPayParams.put("ItemName", ITEM_NAME);
        ecPayParams.put("ReturnURL", RETURN_URL);
        ecPayParams.put("ChoosePayment", "ATM");
        ecPayParams.put("CheckMacValue", CodeUtil.generateCheckMacValue(ecPayParams, HASH_KEY, HASH_IV));
        ecPayParams.put("EncryptType", 1);

        log.info("[BOOK-009] 訂單編號為 {} 的訂單，組合付款參數成功", orderId);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK009Tranrs(ecPayParams));
    }
}
