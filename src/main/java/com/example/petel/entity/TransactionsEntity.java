package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_TRANSACTIONS")
public class TransactionsEntity {

    /**
     * Table ID
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 訂單編號
     */
    @Column(name = "ORDER_ID", nullable = false)
    private String orderId;

    /**
     * 支付方式
     */
    @Column(name = "PAYMENT_ID", nullable = false)
    private String paymentId;

    /**
     * 交易編號：綠界提供
     */
    @Column(name = "TXN_ID", nullable = false)
    private String transactionId;

    /**
     * 金流導向
     */
    @Column(name = "FLOW_TYPE", nullable = false)
    private String flowType;

    /**
     * 付款狀態
     */
    @Column(name = "STATUS", nullable = false)
    private String status;

    /**
     * Idempotency Key
     */
    @Column(name = "IDEMPOTENCY_KEY", nullable = false)
    private String idempotencyKey;

    /**
     * 交易金額
     */
    @Column(name = "HOTEL_CHARGES", nullable = false)
    private Integer hotelCharges;

    /**
     * 交易手續費
     */
    @Column(name = "TXN_FEE", nullable = false)
    private Integer transactionFee;

    /**
     * 退款金額
     */
    @Column(name = "REFUND_AMOUNT")
    private Integer refundAmount;

    /**
     * 支付時間
     */
    @Column(name = "PAY_TIME")
    private LocalDateTime payTime;

    /**
     * 交易成立時間
     */
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 交易更新時間
     */
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}

