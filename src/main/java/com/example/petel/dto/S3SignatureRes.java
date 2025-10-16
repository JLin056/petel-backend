package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 獲取上傳簽名 Response (支援單檔與多檔上傳)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3SignatureRes implements Serializable {
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
     * 上傳資訊列表
     */
    @JsonProperty("uploads")
    private List<S3SignatureResUrlInfo> uploads;
}
