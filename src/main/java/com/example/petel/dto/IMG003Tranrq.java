package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-003 圖片刪除 Request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (要刪除的圖片ID)
     */
    @NotNull(message = "媒體ID不得為空")
    @JsonProperty("mediaId")
    private Long mediaId;
}
