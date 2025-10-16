package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * IMG-001 圖片上傳確認 Response (支援單檔與批量上傳)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG001Tranrs implements Serializable {

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
     * 成功上傳數量
     */
    @JsonProperty("successCount")
    private Integer successCount;

    /**
     * 失敗上傳數量
     */
    @JsonProperty("failedCount")
    private Integer failedCount;

    /**
     * 媒體上傳結果列表
     */
    @JsonProperty("results")
    private List<IMG001TranrsMediaResult> results;
}