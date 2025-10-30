package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MERCH004 房型圖片資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MERCH005TranrqRoomImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 媒體ID
     */
    @JsonProperty("mediaId")
    @NotBlank(message = "mediaId不得為空")
    private String mediaId;

    /**
     * 排序順序
     */
    @JsonProperty("sortOrder")
    private Integer sortOrder;
}
