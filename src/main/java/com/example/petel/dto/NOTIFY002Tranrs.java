package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * NOTIFY-002 查詢通知列表 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知列表
     */
    @JsonProperty("notifications")
    private List<NotificationDto> notifications;
}
