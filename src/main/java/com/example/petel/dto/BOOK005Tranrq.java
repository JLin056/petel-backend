package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOOK005Tranrq implements Serializable {

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

    @JsonProperty("idempotency_key")
    private String idempotencyKey;

    @JsonProperty("hotel_charges")
    private Integer hotelCharges;

    @JsonProperty("txn_fee")
    private Integer transactionFee;

    @JsonProperty("pay_time")
    private Timestamp payTime;

    @JsonProperty("created_at")
    private Timestamp createdAt;
}