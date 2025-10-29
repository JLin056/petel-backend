package com.example.petel.service.impl;

import com.example.petel.dto.*;
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
import com.example.petel.service.BOOK005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * BOOK-005 組合付款參數 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK005SvcImpl implements BOOK005Svc {

    /** TransactionsRepository */
    private final TransactionsRepository transactionsRepository;
    /** OrdersRepository */
    private final OrdersRepository ordersRepository;
    /** PAYMENT_ID */
    private static final String PAYMENT_ID = "Y000000001";
//    /** MERCHANT_ID */
//    @Value("${ecpay.merchantId}")
//    private String MERCHANT_ID;
//    /** HASH_KEY */
//    @Value("${ecpay.hashKey}")
//    private String HASH_KEY;
//    /** HASH_IV */
//    @Value("${ecpay.hashIv}")
//    private String HASH_IV;
//    /** RETURN_URL */
//    @Value("${ecpay.authorize.returnUrl}")
//    private String RETURN_URL;
//    /** ITEM_NAME */
//    private static final String ITEM_NAME = "PETEL room(s)";
//    /** RestTemplate */
//    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 組合付款參數 for 現場付款：信用卡預先授權
     *
     * @param requestBody Req<BOOK005Tranrq>
     * @return Res<BOOK005Tranrs>
     * @throws Exception 拋出例外
     */
    @Override
    public Res<BOOK005Tranrs> book005(Req<BOOK005Tranrq> requestBody) throws Exception {

        log.info("-------- [BOOK-005] 組合付款參數 (現場付款) API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        OrdersEntity ordersEntity = ordersRepository.findById(orderId).orElseThrow(() -> {
            log.error("[BOOK-005] 查無訂單編號為 {} 的資料，無法組合付款參數", orderId);
            return new DataNotFoundException();
        });

        if (!PAYMENT_ID.equals(ordersEntity.getPaymentId())) {
            log.warn("[BOOK-005] 您的付款方式不適用這隻 API");
            throw new InvalidPaymentMethodException();
        }

//        For company use, cancel the following code:

//        JSONObject orderInfoJson = new JSONObject();
//        orderInfoJson.put("MerchantTradeNo", "PETEL" + orderId);
//        orderInfoJson.put("MerchantTradeDate", TimeUtil.getMerchantTradeDate(ordersEntity.getCreatedAt()));
//        orderInfoJson.put("TotalAmount", ordersEntity.getHotelCharges());
//        orderInfoJson.put("ItemName", ITEM_NAME);
//        orderInfoJson.put("TradeDesc", "建立訂單");
//        orderInfoJson.put("ReturnURL", RETURN_URL);
//
//        BOOK005TranrqCardInfo cardInfo = requestBody.getTranrq().getCardInfo();
//        JSONObject cardInfoJson = new JSONObject();
//        cardInfoJson.put("CardNo", cardInfo.getCardNo());
//        cardInfoJson.put("CardValidMM", cardInfo.getCardValidMM());
//        cardInfoJson.put("CardValidYY", cardInfo.getCardValidYY());
//        cardInfoJson.put("CardCVV2", cardInfo.getCardCVV2());
//
//        BOOK005TranrqConsumerInfo consumerInfo = requestBody.getTranrq().getConsumerInfo();
//        JSONObject consumerInfoJson = new JSONObject();
//        consumerInfoJson.put("Phone", consumerInfo.getPhone());
//        consumerInfoJson.put("Name", consumerInfo.getName());
//
//        JSONObject dataJson = new JSONObject();
//        dataJson.put("MerchantID", MERCHANT_ID);
//        dataJson.put("OrderInfo", orderInfoJson);
//        dataJson.put("ChoosePayment", "Credit");
//        dataJson.put("CardInfo", cardInfoJson);
//        dataJson.put("ConsumerInfo", consumerInfoJson);
//
//        JSONObject rqHeader = new JSONObject();
//        rqHeader.put("Timestamp", ZonedDateTime.now(ZoneId.of("Asia/Taipei")).toEpochSecond());
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("MerchantID", MERCHANT_ID);
//        jsonObject.put("RqHeader", rqHeader);
//        jsonObject.put("Data", CodeUtil.dataEncrypt(dataJson.toString(), HASH_KEY, HASH_IV));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                "https://ecpayment-stage.ecpay.com.tw/1.0.0/Cashier/BackAuth",
//                request,
//                String.class);

        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        ordersEntity.setStatus("已授權");
        ordersEntity.setUpdatedAt(now);

        TransactionsEntity transactionsEntity = new TransactionsEntity();

        transactionsEntity.setId(IdUtil.generateTableId("T", transactionsRepository.findMaxId()));
        transactionsEntity.setOrderId(orderId);
        transactionsEntity.setPaymentId(PAYMENT_ID);
        transactionsEntity.setTransactionId("PETEL" + orderId); // 因為無法 call 幕後交易 api
        transactionsEntity.setFlowType("Pay");
        transactionsEntity.setHotelCharges(ordersEntity.getHotelCharges()); // 因為無法 call 幕後交易 api
        transactionsEntity.setCreatedAt(now);
        transactionsEntity.setStatus("授權成功");
        transactionsEntity.setIdempotencyKey(UUID.randomUUID().toString());
        transactionsEntity.setTransactionFee(0);
        transactionsEntity.setPayTime(now);

        try {
            ordersRepository.save(ordersEntity);
        } catch (Exception e) {
            log.error("[BOOK-005] 更新過程有問題，修改訂單編號為 {} 的訂單資料失敗", orderId);
            throw new UpdateFailException();
        }

        try {
            transactionsRepository.save(transactionsEntity);
        } catch (Exception e) {
            log.error("[BOOK-005] 新增資料有問題，新增訂單編號為 {} 的交易資料失敗", orderId);
            throw new InsertFailException();
        }

        log.info("[BOOK-005] 訂單編號為 {} 的訂單，信用卡幕後授權 API 執行完成", orderId);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOK005Tranrs("OK"));
    }
}
