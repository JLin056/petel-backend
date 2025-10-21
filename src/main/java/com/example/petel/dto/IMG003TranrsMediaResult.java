package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-003 單個媒體刪除結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG003TranrsMediaResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (varchar2(10))
     */
    @JsonProperty("mediaId")
    private String mediaId;

    /**
     * 刪除狀態 (SUCCESS, FAILED)
     */
    @JsonProperty("status")
    private String status;

    /**
     * 結果訊息
     */
    @JsonProperty("message")
    private String message;

    /**
     * 錯誤訊息 (如果失敗)
     */
    @JsonProperty("errorMessage")
    private String errorMessage;
}
