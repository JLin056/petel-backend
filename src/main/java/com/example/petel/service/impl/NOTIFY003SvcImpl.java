package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationsRepository;
import com.example.petel.service.NOTIFY003Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * NOTIFY-003 標記通知為已讀 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY003SvcImpl implements NOTIFY003Svc {

    private final NotificationsRepository notificationsRepository;

    /**
     * 將指定通知標記為已讀
     *
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return Res<NOTIFY003Tranrs>
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<NOTIFY003Tranrs> notify003(String accountId, Req<NOTIFY003Tranrq> requestBody) {

        log.info("-------- [NOTIFY-003] 標記通知為已讀 API 啟動 --------");

        String notificationId = requestBody.getTranrq().getNotificationId();

        // 查詢通知
        Optional<NotificationsEntity> optional = notificationsRepository.findById(notificationId);
        if (optional.isEmpty()) {
            log.warn("[NOTIFY-003] 通知不存在，通知ID：{}", notificationId);
            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.DATA_NOT_FOUND), new NOTIFY003Tranrs(false, null));
        }

        NotificationsEntity notification = optional.get();

        // 驗證權限：只能標記自己的通知
        if (!notification.getAccountId().equals(accountId)) {
            log.warn("[NOTIFY-003] 無權限標記此通知，帳號ID：{}, 通知ID：{}", accountId, notificationId);
            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.FORBIDDEN), new NOTIFY003Tranrs(false, null));
        }

        // 如果已經是已讀狀態，直接返回成功
        if ("READ".equals(notification.getStatus())) {
            log.info("[NOTIFY-003] 通知已是已讀狀態，通知ID：{}", notificationId);
            String readAt = notification.getReadAt() != null ? notification.getReadAt().toString() : null;
            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY003Tranrs(true, readAt));
        }

        // 更新為已讀
        OffsetDateTime readAt = OffsetDateTime.now();
        notification.setStatus("READ");
        notification.setReadAt(readAt);
        notificationsRepository.save(notification);

        log.info("[NOTIFY-003] 標記通知為已讀成功，通知ID：{}", notificationId);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY003Tranrs(true, readAt.toString()));
    }
}
