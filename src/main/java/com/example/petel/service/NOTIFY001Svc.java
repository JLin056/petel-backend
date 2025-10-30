package com.example.petel.service;

import com.example.petel.dto.NOTIFY001Tranrq;
import com.example.petel.dto.NOTIFY001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * NOTIFY-001 發送通知
 */
public interface NOTIFY001Svc {
    /**
     * 發送通知給指定帳號
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return 回應內容（包含通知 ID）
     */
    Res<NOTIFY001Tranrs> notify001(String accountId, Req<NOTIFY001Tranrq> requestBody);
}
