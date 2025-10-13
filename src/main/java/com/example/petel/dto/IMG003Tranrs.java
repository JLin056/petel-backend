package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-003 圖片刪除 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG003Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 被刪除的媒體ID
     */
    @JsonProperty("mediaId")
    private Long mediaId;

    /**
     * 刪除結果訊息
     */
    @JsonProperty("message")
    private String message;
}
