package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IMG-003 單個媒體刪除資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMG003TranrqMediaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID (要刪除的圖片ID, varchar2(10))
     */
    @NotBlank(message = "媒體ID不得為空")
    @JsonProperty("mediaId")
    private String mediaId;
}
