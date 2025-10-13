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

    /**
     * merchantId
     */
    @JsonProperty("MerchantID")
    private Long merchantId;

    /**
     * rpHeader
     */
    @JsonProperty("RpHeader")
    private BOOK010TranrqRpHeader rpHeader;

    /**
     * transCode
     */
    @JsonProperty("TransCode")
    private Integer transCode;

    /**
     * transMsg
     */
    @JsonProperty("TransMsg")
    private String transMsg;

    /**
     * data
     */
    @JsonProperty("Data")
    private String data;
}