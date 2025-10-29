package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationsRepository;
import com.example.petel.service.NOTIFY002Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NOTIFY-002 查詢通知列表 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY002SvcImpl implements NOTIFY002Svc {

    private final NotificationsRepository notificationsRepository;

    /**
     * 查詢指定帳號的所有通知列表
     *
     * @param accountId 帳號 ID
     * @return Res<NOTIFY002Tranrs>
     */
    @Override
    public Res<NOTIFY002Tranrs> notify002(String accountId) {

        log.info("-------- [NOTIFY-002] 查詢通知列表 API 啟動 --------");

        // 查詢通知列表（依建立時間降序）
        List<NotificationsEntity> notifications = notificationsRepository.findByAccountIdOrderByCreatedAtDesc(accountId);

        // 轉換為 DTO
        List<NotificationDto> notificationDtos = notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("[NOTIFY-002] 查詢通知列表成功，共 {} 筆通知", notificationDtos.size());

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY002Tranrs(notificationDtos));
    }

    /**
     * 將 Entity 轉換為 DTO
     */
    private NotificationDto convertToDto(NotificationsEntity entity) {
        NotificationDto dto = new NotificationDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setReadAt(entity.getReadAt() != null ? entity.getReadAt().toString() : null);
        dto.setOrderId(entity.getOrderId());
        dto.setPropertyId(entity.getPropertyId());
        dto.setSellerId(entity.getSellerId());
        return dto;
    }
}
