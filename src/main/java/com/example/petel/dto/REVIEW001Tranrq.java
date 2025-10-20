package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class REVIEW001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderId
     */
    @JsonProperty("orderId")
    @NotBlank(message = "orderId 不得為空")
    private String orderId;

    /**
     * priceScore
     */
    @JsonProperty("priceScore")
    @NotNull(message = "priceScore 不得為空")
    private Integer priceScore;

    /**
     * envScore
     */
    @JsonProperty("envScore")
    @NotNull(message = "envScore 不得為空")
    private Integer envScore;

    /**
     * serviceScore
     */
    @JsonProperty("serviceScore")
    @NotNull(message = "serviceScore 不得為空")
    private Integer serviceScore;

    /**
     * content
     */
    @JsonProperty("content")
    private String content;
}
