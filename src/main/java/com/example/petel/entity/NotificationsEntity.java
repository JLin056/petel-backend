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
@Table(name = "PETEL_NOTIFICATIONS")
public class NotificationsEntity {

    /**
     * 通知編號
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
     * 通知標題
     */
    @Column(name = "TITLE", nullable = false, length = 120)
    private String title;

    /**
     * 通知訊息內容
     */
    @Column(name = "MESSAGE", nullable = false, length = 1000)
    private String message;

    /**
     * 通知類型：SYSTEM(平台維護公告) / ORDER(訂單相關) / PAYMENT(交易相關)
     */
    @Column(name = "TYPE", nullable = false, length = 30)
    private String type;

    /**
     * 通知狀態：UNREAD(未讀) / READ(已讀)
     */
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;

    /**
     * 通知建立時間
     */
    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;

    /**
     * 通知已讀時間
     */
    @Column(name = "READ_AT")
    private OffsetDateTime readAt;

    /**
     * 關聯的訂單編號（可選）
     */
    @Column(name = "ORDER_ID", length = 10)
    private String orderId;

    /**
     * 關聯的旅館編號（可選）
     */
    @Column(name = "PROPERTY_ID", length = 10)
    private String propertyId;

    /**
     * 關聯的賣家編號（可選）
     */
    @Column(name = "SELLER_ID", length = 10)
    private String sellerId;
}
