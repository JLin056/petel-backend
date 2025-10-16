package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * IMG-003 圖片刪除 Response (支援單檔與批量刪除)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG003Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案分類/前綴 (User_Profile, Property_Facility, Room_Image)
     */
    @JsonProperty("category")
    private String category;

    /**
     * 關聯ID (選填，例如：propertyId, userId, roomId)
     */
    @JsonProperty("referenceId")
    private String referenceId;

    /**
     * 成功刪除數量
     */
    @JsonProperty("successCount")
    private Integer successCount;

    /**
     * 刪除失敗數量
     */
    @JsonProperty("failedCount")
    private Integer failedCount;

    /**
     * 刪除結果列表
     */
    @JsonProperty("results")
    private List<IMG003TranrsMediaResult> results;
}
