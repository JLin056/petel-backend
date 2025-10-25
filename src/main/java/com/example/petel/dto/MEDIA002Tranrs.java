package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-002 Base64 圖片更新 Response (支援批量更新)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功更新數量
     */
    @JsonProperty("successCount")
    private Integer successCount;

    /**
     * 失敗更新數量
     */
    @JsonProperty("failedCount")
    private Integer failedCount;

    /**
     * 媒體更新結果列表
     */
    @JsonProperty("results")
    private List<MEDIA002TranrsMediaResult> results;
}
