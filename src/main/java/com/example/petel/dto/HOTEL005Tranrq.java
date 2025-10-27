package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HOTEL005Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 旅館編號（必填）
     */
    @JsonProperty("propertyId")
    @NotBlank(message = "propertyId不得為空")
    private String propertyId;

    /**
     * 寵物種類（必填，CAT 或 DOG）
     */
    @JsonProperty("petType")
    @NotBlank(message = "petType不得為空")
    private String petType;

    /**
     * 入住日期（選填，格式：YYYY-MM-DD）
     */
    @JsonProperty("checkIn")
    private String checkIn;

    /**
     * 退房日期（選填，格式：YYYY-MM-DD）
     */
    @JsonProperty("checkOut")
    private String checkOut;

    /**
     * 寵物數量（選填，預設為1）
     */
    @JsonProperty("petCount")
    private Integer petCount;
}
