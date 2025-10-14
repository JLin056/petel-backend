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

    /**
     * orderId
     */
    @JsonProperty("order_id")
    private String orderId;

    /**
     * paymentId
     */
    @JsonProperty("payment_id")
    private String paymentId;

    /**
     * transactionId
     */
    @JsonProperty("txn_id")
    private String transactionId;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

    /**
     * idempotencyKey
     */
    @JsonProperty("idempotency_key")
    private String idempotencyKey;

    /**
     * hotelCharges
     */
    @JsonProperty("hotel_charges")
    private Integer hotelCharges;

    /**
     * transactionFee
     */
    @JsonProperty("txn_fee")
    private Integer transactionFee;

    /**
     * payTime
     */
    @JsonProperty("pay_time")
    private Timestamp payTime;

    /**
     * createdAt
     */
    @JsonProperty("created_at")
    private Timestamp createdAt;
}