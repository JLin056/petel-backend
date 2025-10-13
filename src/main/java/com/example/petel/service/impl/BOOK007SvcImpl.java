package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.TimeUtil;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK007Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK007SvcImpl implements BOOK007Svc {

//    /** OrdersRepository */
//    private final OrdersRepository ordersRepository;
//    /** MERCHANT_ID */
//    private static final String MERCHANT_ID = "3002607"; // 初期採用綠界測試帳號
//    /** RETURN_URL */
//    private static final String RETURN_URL = "http://localhost:8080/bookings/credit/notify"; // TODO 外界可以進入的URL
//    /** HASH_KEY */
//    private static final String HASH_KEY = "pwFHCqoQZGmho4w6"; // 初期採用綠界測試帳號
//    /** HASH_IV */
//    private static final String HASH_IV = "EkRm7iFT261dpevs"; // 初期採用綠界測試帳號
//    /** ITEM_NAME */
//    private static final String ITEM_NAME = "PETEL room(s)";

    @Override
    public Res<BOOK007Tranrs> book007(Req<BOOK007Tranrq> requestBody) throws Exception {

//        long orderId = requestBody.getTranrq().getOrderId();
//
//        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
//            log.error("[BOOK-007] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
//            return new DataNotFoundException();
//        });
//
//        Map<String, Object> ecPayParams = new HashMap<>();
//        ecPayParams.put("MerchantID", MERCHANT_ID);
//        ecPayParams.put("MerchantTradeNo", "PETEL" + String.format("%015d", orderId));
//        ecPayParams.put("MerchantTradeDate", TimeUtil.getMerchantTradeDate(ordersEntity.getCreatedAt()));
//        ecPayParams.put("PaymentType", "aio");
//        ecPayParams.put("TotalAmount", ordersEntity.getHotelCharges());
//        ecPayParams.put("TradeDesc", "建立訂單");
//        ecPayParams.put("ItemName", ITEM_NAME);
//        ecPayParams.put("ReturnURL", RETURN_URL);
//        ecPayParams.put("ChoosePayment", "Credit");
//        ecPayParams.put("CheckMacValue", CodeUtil.generateCheckMacValue(ecPayParams, HASH_KEY, HASH_IV));
//        ecPayParams.put("EncryptType", 1);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK008Tranrs(ecPayParams));

    }
}
