package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-006 補發錯過的事件 Request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上次收到事件的時間（ISO 8601 格式）
     * 例如：2024-01-01T12:00:00+08:00
     */
    @NotBlank(message = "上次事件時間不可為空")
    @JsonProperty("last_event_time")
    private String lastEventTime;
}
