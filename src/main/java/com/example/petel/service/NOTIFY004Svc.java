package com.example.petel.service;

import com.example.petel.dto.NOTIFY004Tranrs;
import com.example.petel.dto.Res;

/**
 * NOTIFY-004 統計未讀通知數量
 */
public interface NOTIFY004Svc {
    /**
     * 統計指定帳號的未讀通知數量
     * @param accountId 帳號 ID
     * @return 回應內容（包含未讀數量）
     */
    Res<NOTIFY004Tranrs> notify004(String accountId);
}
