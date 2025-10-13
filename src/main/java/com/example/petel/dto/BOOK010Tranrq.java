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
public class BOOK010Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("MerchantID")
    private Long merchantId;

    @JsonProperty("RpHeader")
    private BOOK010TranrqRpHeader rpHeader;

    @JsonProperty("TransCode")
    private Integer transCode;

    @JsonProperty("TransMsg")
    private String transMsg;

    @JsonProperty("Data")
    private String data;
}