package com.example.petel.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * NOTIFY-005 建立 SSE 連線（Server-Sent Events）
 */
public interface NOTIFY005Svc {
    /**
     * 為指定帳號建立 SSE 連線
     * @param accountId 帳號 ID
     * @return SseEmitter 實例
     */
    SseEmitter notify005(String accountId);
}
