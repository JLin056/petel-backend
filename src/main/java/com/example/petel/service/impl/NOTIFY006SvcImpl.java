package com.example.petel.service.impl;

import com.example.petel.component.NotificationHub;
import com.example.petel.dto.*;
import com.example.petel.entity.NotificationEventsEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.NotificationEventsRepository;
import com.example.petel.service.NOTIFY006Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NOTIFY-006 補發錯過的事件 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NOTIFY006SvcImpl implements NOTIFY006Svc {

    private final NotificationEventsRepository notificationEventsRepository;
    private final NotificationHub notificationHub;
    private final ObjectMapper objectMapper;

    /**
     * 重新連線時補發錯過的事件
     *
     * @param accountId 帳號 ID
     * @param requestBody 請求內容
     * @return Res<NOTIFY006Tranrs>
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<NOTIFY006Tranrs> notify006(String accountId, Req<NOTIFY006Tranrq> requestBody) {

        log.info("-------- [NOTIFY-006] 補發錯過的事件 API 啟動 --------");

        String lastEventTimeStr = requestBody.getTranrq().getLastEventTime();

        // 解析時間
        OffsetDateTime lastEventTime;
        try {
            lastEventTime = OffsetDateTime.parse(lastEventTimeStr);
        } catch (Exception e) {
            log.error("[NOTIFY-006] 時間格式錯誤：{}", lastEventTimeStr, e);
            return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.ERROR_INPUT), new NOTIFY006Tranrs(0));
        }

        // 查詢在指定時間之後的所有事件
        List<NotificationEventsEntity> missedEvents =
                notificationEventsRepository.findByAccountIdAndSentAtAfterOrderBySentAtAsc(accountId, lastEventTime);

        int count = 0;
        for (NotificationEventsEntity event : missedEvents) {
            try {
                // 解析 payload 並重新發送
                @SuppressWarnings("unchecked")
                Map<String, Object> payload = objectMapper.readValue(
                        event.getPayload(),
                        objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class)
                );
                notificationHub.sendTo(accountId, event.getEventName(), event.getEventId(), payload);

                // 更新為已送達
                if ("N".equals(event.getDelivered())) {
                    event.setDelivered("Y");
                    notificationEventsRepository.save(event);
                }
                count++;
            } catch (Exception e) {
                log.error("[NOTIFY-006] 補發事件失敗，事件ID：{}", event.getEventId(), e);
            }
        }

        log.info("[NOTIFY-006] 補發錯過的事件成功，帳號ID：{}, 補發數量：{}", accountId, count);

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new NOTIFY006Tranrs(count));
    }
}
