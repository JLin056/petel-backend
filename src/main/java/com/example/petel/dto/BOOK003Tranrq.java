package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BOOK003Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("note")
    private String note;
}

