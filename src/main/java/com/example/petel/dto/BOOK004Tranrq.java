package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK004Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    @NotNull(message = "order_id不得為空")
    private Long orderId;
}