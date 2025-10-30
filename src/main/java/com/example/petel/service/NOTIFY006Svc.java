package com.example.petel.service;

import com.example.petel.dto.NOTIFY006Tranrq;
import com.example.petel.dto.NOTIFY006Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * NOTIFY-006 補發錯過的事件
 */
public interface NOTIFY006Svc {
    /**
     * 重新連線時補發錯過的事件
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return 回應內容（包含補發的事件數量）
     */
    Res<NOTIFY006Tranrs> notify006(String accountId, Req<NOTIFY006Tranrq> requestBody);
}
