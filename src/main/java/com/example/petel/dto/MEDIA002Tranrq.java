package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-002 Base64 圖片更新 Request (支援批量更新)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA002Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體更新資訊列表
     */
    @NotEmpty(message = "媒體列表不得為空")
    @Valid
    @JsonProperty("medias")
    private List<MEDIA002TranrqMediaInfo> medias;
}
