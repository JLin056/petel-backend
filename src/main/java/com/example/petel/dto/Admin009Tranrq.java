package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin009Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 賣家 ID (必填)
     */
    @NotNull(message = "賣家 ID 不得為空")
    @JsonProperty("sellerId")
    private String sellersId;
}
