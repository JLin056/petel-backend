package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "arrival_date不得為空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式錯誤，須為 yyyy-MM-dd")
    private String arrivalDate;

    @JsonProperty("product_quantity")
    @NotNull(message = "product_quantity不得為空")
    private Integer productQuantity;

    @JsonProperty("product_price")
    @NotNull(message = "product_price不得為空")
    private Integer productPrice;
}

