package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT001Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 訂單 ID
     */
    @JsonProperty("orderId")
    @NotBlank(message = "orderId 不得為空")
    private String orderId;
}
