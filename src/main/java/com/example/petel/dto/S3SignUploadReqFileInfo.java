package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 單個檔案上傳資訊 S3SignUploadReqFileInfo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3SignUploadReqFileInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 檔案名稱
     */
    @NotBlank(message = "檔案名稱不得為空")
    @JsonProperty("filename")
    private String filename;

    /**
     * 檔案類型 (MIME type)
     */
    @NotBlank(message = "檔案類型不得為空")
    @JsonProperty("fileType")
    private String fileType;
}
