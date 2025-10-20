package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ADMIN005Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單 ID
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 取消後的訂單狀態
     */
    @JsonProperty("status")
    private String status;

    /**
     * 更新時間
     */
    @JsonProperty("updatedAt")
    private String updatedAt;

    /**
     * 訊息
     */
    @JsonProperty("message")
    private String message;
}
