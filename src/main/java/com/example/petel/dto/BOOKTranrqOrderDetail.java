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
public class BOOKTranrqOrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("product_id")
    @NotNull(message = "product_id不得為空")
    private Long productId;

    @JsonProperty("arrival_date")
    @NotNull(message = "arrival_date不得為空")
    private String arrivalDate;

    @JsonProperty("product_quantity")
    @NotNull(message = "product_quantity不得為空")
    private Integer productQuantity;

    @JsonProperty("product_price")
    @NotNull(message = "product_price不得為空")
    private Double productPrice;
}

