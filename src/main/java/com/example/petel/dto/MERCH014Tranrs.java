package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MERCH014Tranrs implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("status")
    private String status;
}