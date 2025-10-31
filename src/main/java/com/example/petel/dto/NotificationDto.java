package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通知 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知編號
     */
    @JsonProperty("id")
    private String id;

    /**
     * 通知標題
     */
    @JsonProperty("title")
    private String title;

    /**
     * 通知訊息內容
     */
    @JsonProperty("message")
    private String message;

    /**
     * 通知類型
     */
    @JsonProperty("type")
    private String type;

    /**
     * 通知狀態
     */
    @JsonProperty("status")
    private String status;

    /**
     * 通知建立時間
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * 通知已讀時間
     */
    @JsonProperty("read_at")
    private String readAt;

    /**
     * 關聯的訂單編號
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * 關聯的旅館編號
     */
    @JsonProperty("property_id")
    private String propertyId;

    /**
     * 入住時間
     */
    @JsonProperty("check_in")
    private String checkIn;

    /**
     * 退房時間
     */
    @JsonProperty("check_out")
    private String checkOut;

    /**
     * 旅館名稱（ORDER 類型通知時提供）
     */
    @JsonProperty("property_name")
    private String propertyName;

    /**
     * 用戶名稱（ORDER 類型通知時提供）
     */
    @JsonProperty("user_name")
    private String userName;
}
