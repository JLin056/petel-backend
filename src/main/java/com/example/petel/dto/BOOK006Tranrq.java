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
public class BOOK006Tranrq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * orderId
     */
    @JsonProperty("order_id")
    private Long orderId;

    /**
     * paymentId
     */
    @JsonProperty("payment_id")
    private Integer paymentId;

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
     * refundRatio
     */
    @JsonProperty("refund_ratio")
    private Double refundRatio;
}