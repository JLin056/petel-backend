package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * NOTIFY-003 標記通知為已讀 Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NOTIFY003Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 操作是否成功
     */
    @JsonProperty("success")
    private Boolean success;

    /**
     * 已讀時間
     */
    @JsonProperty("read_at")
    private String readAt;
}
