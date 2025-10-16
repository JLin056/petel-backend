
package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * IMG-001 圖片上傳確認 Request (支援單檔與多檔上傳)
 * 使用 Presigned URL 上傳後的確認
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案分類/前綴 (User_Profile, Property_Facility, Room_Image)
     */
    @NotBlank(message = "檔案分類不得為空")
    @JsonProperty("category")
    private String category;

    /**
     * 關聯ID (選填，例如：propertyId, userId, roomId)
     */
    @JsonProperty("referenceId")
    private String referenceId;

    /**
     * 媒體上傳資訊列表
     */
    @NotEmpty(message = "媒體列表不得為空")
    @Valid
    @JsonProperty("medias")
    private List<IMG001TranrqMediaInfo> medias;
}
