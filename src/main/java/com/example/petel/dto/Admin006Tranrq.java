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
public class Admin006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 物業 ID (必填)
     */
    @NotNull(message = "物業 ID 不得為空")
    @JsonProperty("propertyId")
    private Integer propertyId;
}
