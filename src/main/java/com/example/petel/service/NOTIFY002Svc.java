package com.example.petel.service;

import com.example.petel.dto.NOTIFY002Tranrs;
import com.example.petel.dto.Res;

/**
 * NOTIFY-002 查詢通知列表
 */
public interface NOTIFY002Svc {
    /**
     * 查詢指定帳號的所有通知列表
     * @param accountId 帳號 ID
     * @return 回應內容（包含通知列表）
     */
    Res<NOTIFY002Tranrs> notify002(String accountId);
}
