package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.service.BOOK005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * BOOK-005 組合付款參數 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK005SvcImpl implements BOOK005Svc {

    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000001";
    /** MERCHANT_ID */
    @Value("${ecpay.merchantId}")
    private String MERCHANT_ID;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;

    /**
     * 組合付款參數 for 現場付款：信用卡預先授權
     *
     * @param requestBody Req<BOOK005Tranrq>
     * @return Res<BOOK005Tranrs>
     * @throws Exception 拋出例外
     */
    @Override
    public Res<BOOK005Tranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception {

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-005] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
            return new DataNotFoundException();
        });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.error("[BOOK-005] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

        log.info("[BOOK-005] 訂單編號為 {} 的訂單，組合付款參數成功", orderId);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK005Tranrs(MERCHANT_ID, new BOOK005TranrsRqHeader(ZonedDateTime.now(ZoneId.of("Asia/Taipei")).toEpochSecond()), CodeUtil.dataEncrypt(requestBody.getTranrq().getData(), HASH_KEY, HASH_IV)));
    }
}
