package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-001 發送通知 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知 ID
     */
    @JsonProperty("notification_id")
    private String notificationId;
}
