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
public class ADMIN005Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單 ID
     */
    @NotBlank(message = "訂單 ID 不可為空")
    @JsonProperty("orderId")
    private String orderId;
}
