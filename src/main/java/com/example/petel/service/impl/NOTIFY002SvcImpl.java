package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.NotificationsEntity;
import com.example.petel.entity.OrdersEntity;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.UsersEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationsRepository;
import com.example.petel.repository.OrdersRepository;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.NOTIFY002Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * NOTIFY-002 查詢通知列表 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY002SvcImpl implements NOTIFY002Svc {

    private final NotificationsRepository notificationsRepository;
    private final OrdersRepository ordersRepository;
    private final PropertyRepository propertyRepository;
    private final UsersRepository usersRepository;

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


        // 如果是 ORDER 類型且有 orderId，則查詢關聯的 propertyName 和 userName
        if ("ORDER".equals(entity.getType()) && entity.getOrderId() != null) {
            try {
                Optional<OrdersEntity> orderOpt = ordersRepository.findById(entity.getOrderId());
                if (orderOpt.isPresent()) {
                    OrdersEntity order = orderOpt.get();
                    dto.setCheckIn(order.getCheckIn());
                    dto.setCheckOut(order.getCheckOut());

                    // 查詢 Property Name
                    if (order.getPropertyId() != null) {
                        Optional<PropertyEntity> propertyOpt = propertyRepository.findById(order.getPropertyId());
                        propertyOpt.ifPresent(property -> {
                            dto.setPropertyName(property.getName());
                            dto.setPropertyId(property.getId());
                        });
                    }

                    // 查詢 User Name
                    if (order.getUserId() != null) {
                        Optional<UsersEntity> userOpt = usersRepository.findById(order.getUserId());
                        userOpt.ifPresent(user -> dto.setUserName(user.getName()));
                    }
                }
            } catch (Exception e) {
                log.warn("[NOTIFY-002] 查詢訂單相關資料失敗，訂單ID: {}", entity.getOrderId(), e);
            }
        }

        return dto;
    }
}
