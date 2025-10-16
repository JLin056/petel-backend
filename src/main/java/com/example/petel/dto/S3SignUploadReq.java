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
 * 獲取上傳簽名 Request (支援單檔與多檔上傳)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3SignUploadReq implements Serializable {
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
     * 用於在 S3 中建立子資料夾，方便管理
     */
    @JsonProperty("referenceId")
    private String referenceId;

    /**
     * 檔案列表 (支援單個或多個檔案)
     */
    @NotEmpty(message = "檔案列表不得為空")
    @Valid
    @JsonProperty("files")
    private List<S3SignUploadReqFileInfo> files;
}
