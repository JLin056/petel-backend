package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-003 Base64 圖片刪除 Request (支援批量刪除)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 要刪除的媒體ID列表
     */
    @NotEmpty(message = "媒體ID列表不得為空")
    @JsonProperty("mediaIds")
    private List<String> mediaIds;
}
