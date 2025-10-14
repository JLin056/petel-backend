package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_ORDERS")
public class OrdersEntity {

    /**
     * 訂單編號
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 用戶編號
     */
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    /**
     * 旅館編號
     */
    @Column(name = "PROPERTY_ID", nullable = false)
    private String propertyId;

    /**
     * 支付方式（編號）
     */
    @Column(name = "PAYMENT_ID", nullable = false)
    private String paymentId;

    /**
     * 訂單費用
     */
    @Column(name = "HOTEL_CHARGES", nullable = false)
    private Integer hotelCharges;

    /**
     * 入住日期：'yyyy-MM-dd'
     */
    @Column(name = "CHECK_IN", nullable = false)
    private String checkIn;

    /**
     * 退房日期：'yyyy-MM-dd'
     */
    @Column(name = "CHECK_OUT", nullable = false)
    private String checkOut;

    /**
     * 訂單狀態
     */
    @Column(name = "STATUS", nullable = false)
    private String status;

    /**
     * 訂單備註
     */
    @Column(name = "NOTE")
    private String note;

    /**
     * 訂單成立時間
     */
    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp createdAt;

    /**
     * 訂單更新時間
     */
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
}