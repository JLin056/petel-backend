package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MEDIA-003 單個媒體 Base64 刪除結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA003TranrsMediaResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * 刪除狀態 (SUCCESS, FAILED)
     */
    @JsonProperty("status")
    private String status;

    /**
     * 錯誤訊息 (如果失敗)
     */
    @JsonProperty("errorMessage")
    private String errorMessage;
}
