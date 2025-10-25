package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-003 Base64 圖片刪除 Response (支援批量刪除)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA003Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功刪除數量
     */
    @JsonProperty("successCount")
    private Integer successCount;

    /**
     * 失敗刪除數量
     */
    @JsonProperty("failedCount")
    private Integer failedCount;

    /**
     * 媒體刪除結果列表
     */
    @JsonProperty("results")
    private List<MEDIA003TranrsMediaResult> results;
}
