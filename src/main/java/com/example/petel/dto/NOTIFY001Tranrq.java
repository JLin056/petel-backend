package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-001 發送通知 Request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知標題
     */
    @NotBlank(message = "通知標題不可為空")
    @JsonProperty("title")
    private String title;

    /**
     * 通知訊息內容
     */
    @NotBlank(message = "通知訊息內容不可為空")
    @JsonProperty("message")
    private String message;

    /**
     * 通知類型：SYSTEM(平台維護公告) / ORDER(訂單相關) / PAYMENT(交易相關)
     */
    @NotBlank(message = "通知類型不可為空")
    @JsonProperty("type")
    private String type;

    /**
     * 關聯的訂單編號（可選）
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * 關聯的旅館編號（可選）
     */
    @JsonProperty("property_id")
    private String propertyId;

    /**
     * 關聯的賣家編號（可選）
     */
    @JsonProperty("seller_id")
    private String sellerId;
}
