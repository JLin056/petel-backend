package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class HOTEL005Tranrq implements Serializable {

    @Serial
    private static final long SerialVersionUID = 1L;

    /**
     * sellerId
     */
    @JsonProperty("sellerId")
    @NotNull(message = "sellerId不得為空")
    private Long sellerId;
}
