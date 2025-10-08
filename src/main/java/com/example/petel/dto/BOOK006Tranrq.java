package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class BOOK006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("payment_id")
    private Integer paymentId;

    @JsonProperty("txn_id")
    private String transactionId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("refund_ratio")
    private Double refundRatio;
}