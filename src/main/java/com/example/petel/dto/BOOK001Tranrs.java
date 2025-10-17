package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    private String orderId;
}