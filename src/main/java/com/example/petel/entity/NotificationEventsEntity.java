package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_NOTIFICATION_EVENTS")
public class NotificationEventsEntity {

    /**
     * 事件編號
     */
    @Id
    @Column(name = "ID", nullable = false, length = 10)
    private String id;

    /**
     * 帳號編號
     */
    @Column(name = "ACCOUNT_ID", nullable = false, length = 10)
    private String accountId;

    /**
     * SSE 事件 ID
     */
    @Column(name = "EVENT_ID", nullable = false, length = 120)
    private String eventId;

    /**
     * SSE 事件名稱（例如：notification, order-status）
     */
    @Column(name = "EVENT_NAME", nullable = false, length = 100)
    private String eventName;

    /**
     * SSE 事件內容（JSON 格式）
     */
    @Lob
    @Column(name = "PAYLOAD")
    private String payload;

    /**
     * 事件發送時間
     */
    @Column(name = "SENT_AT", nullable = false)
    private OffsetDateTime sentAt;

    /**
     * 是否已送達：Y(已送達) / N(未送達)
     */
    @Column(name = "DELIVERED", nullable = false, length = 1)
    private String delivered;

    /**
     * 關聯的通知編號（可選）
     */
    @Column(name = "NOTIFICATION_ID", length = 10)
    private String notificationId;
}
