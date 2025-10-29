package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-003 標記通知為已讀 Request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知 ID
     */
    @NotBlank(message = "通知 ID 不可為空")
    @JsonProperty("notification_id")
    private String notificationId;
}
