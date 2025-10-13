package com.example.petel.service.impl;

import com.example.petel.dto.BOOK010Tranrq;
import com.example.petel.service.BOOK010Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BOOK-010 通知付款回應綠界 (現場付款) SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK010SvcImpl implements BOOK010Svc {

    @Override
    public String book010(BOOK010Tranrq requestBody) throws Exception {

        // 先以最低標準實作：只有接收綠界參數，並直接回傳OK
        // TODO 驗證參數（檢查 CheckMacValue 安全碼）
        // TODO 記錄付款結果到系統訂單
        // TODO 依 RtnCode == 1 判斷付款完成，其他代碼為失敗

        return "1|OK";
    }
}
