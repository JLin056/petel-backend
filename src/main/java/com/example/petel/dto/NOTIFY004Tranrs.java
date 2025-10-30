package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-004 統計未讀通知數量 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 未讀通知數量
     */
    @JsonProperty("unread_count")
    private Long unreadCount;
}
