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
public class MERCH013Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * seller id
     */
    @JsonProperty("sellerId")
    @NotBlank(message = "sellerId不得為空")
    private String sellerId;
}
