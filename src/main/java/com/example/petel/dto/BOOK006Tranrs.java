package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK006Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("ecPay_params")
    private Map<String, Object> ecPayParams;
}