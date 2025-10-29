package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationsRepository;
import com.example.petel.service.NOTIFY004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * NOTIFY-004 統計未讀通知數量 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY004SvcImpl implements NOTIFY004Svc {

    private final NotificationsRepository notificationsRepository;

    /**
     * 統計指定帳號的未讀通知數量
     *
     * @param accountId 帳號 ID
     * @return Res<NOTIFY004Tranrs>
     */
    @Override
    public Res<NOTIFY004Tranrs> notify004(String accountId) {

        log.info("-------- [NOTIFY-004] 統計未讀通知數量 API 啟動 --------");

        // 統計未讀數量
        long unreadCount = notificationsRepository.countByAccountIdAndStatus(accountId, "UNREAD");

        log.info("[NOTIFY-004] 統計未讀通知數量成功，帳號ID：{}, 未讀數量：{}", accountId, unreadCount);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY004Tranrs(unreadCount));
    }
}
