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
public class HOTEL006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 旅館編號（必填）
     */
    @JsonProperty("propertyId")
    @NotBlank(message = "propertyId不得為空")
    private String propertyId;


}
