package com.example.petel.service;

import com.example.petel.dto.NOTIFY003Tranrq;
import com.example.petel.dto.NOTIFY003Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

/**
 * NOTIFY-003 標記通知為已讀
 */
public interface NOTIFY003Svc {
    /**
     * 將指定通知標記為已讀
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return 回應內容
     */
    Res<NOTIFY003Tranrs> notify003(String accountId, Req<NOTIFY003Tranrq> requestBody);
}
