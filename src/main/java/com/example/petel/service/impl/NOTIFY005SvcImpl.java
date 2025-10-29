package com.example.petel.service.impl;

import com.example.petel.component.NotificationHub;
import com.example.petel.service.NOTIFY005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * NOTIFY-005 建立 SSE 連線 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY005SvcImpl implements NOTIFY005Svc {

    private final NotificationHub notificationHub;

    /**
     * 為指定帳號建立 SSE 連線
     *
     * @param accountId 帳號 ID
     * @return SseEmitter 實例
     */
    @Override
    public SseEmitter notify005(String accountId) {

        log.info("-------- [NOTIFY-005] 建立 SSE 連線 API 啟動 --------");
        log.info("[NOTIFY-005] 為帳號 {} 建立 SSE 連線", accountId);

        // 透過 NotificationHub 註冊 SSE 連線
        SseEmitter emitter = notificationHub.register(accountId);

        log.info("[NOTIFY-005] SSE 連線建立成功，帳號ID：{}, 目前連線數：{}", accountId, notificationHub.getConnectionCount(accountId));

        return emitter;
    }
}
