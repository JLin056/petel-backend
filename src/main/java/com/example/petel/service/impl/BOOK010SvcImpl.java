package com.example.petel.service.impl;

import com.example.petel.dto.BOOK010Tranrq;
import com.example.petel.exception.PaymentFailedException;
import com.example.petel.model.book.CodeUtil;
import com.example.petel.service.BOOK010Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * BOOK-010 綠界通知付款回應 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK010SvcImpl implements BOOK010Svc {

    /** SUCCESS_RTN_CODE */
    private final int SUCCESS_RTN_CODE = 1;
    /** HASH_KEY */
    @Value("${ecpay.hashKey}")
    private String HASH_KEY;
    /** HASH_IV */
    @Value("${ecpay.hashIv}")
    private String HASH_IV;

    @Override
    public String book010(BOOK010Tranrq requestBody) throws Exception {

        JSONObject jsonObject = new JSONObject(CodeUtil.dataDecrypt(requestBody.getData(), HASH_KEY, HASH_IV)); // TODO As PR, need to say add Maven dependency

        if (Integer.parseInt(jsonObject.getString("RtnCode")) != SUCCESS_RTN_CODE) {
            log.error("[BOOK-010] 付款異常");
            throw new PaymentFailedException();
        }

        return "1|OK";
    }
}
