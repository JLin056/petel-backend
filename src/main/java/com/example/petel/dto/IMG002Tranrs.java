package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * IMG-002 圖片更新 Response (支援單檔與批量更新)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案分類/前綴
     */
    @JsonProperty("category")
    private String category;

    /**
     * 關聯ID
     */
    @JsonProperty("referenceId")
    private String referenceId;

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
    private List<IMG002TranrsMediaResult> results;
}
