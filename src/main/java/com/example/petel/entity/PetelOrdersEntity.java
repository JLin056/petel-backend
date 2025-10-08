package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "PETEL_ORDERS")
public class PetelOrdersEntity {

    /**
     * 訂單編號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用戶編號
     */
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    /**
     * 旅館編號
     */
    @Column(name = "PROPERTY_ID", nullable = false)
    private Long propertyId;

    /**
     * 支付方式（編號）
     */
    @Column(name = "PAYMENT_ID", nullable = false)
    private Integer paymentId;

    /**
     * 訂單費用
     */
    @Column(name = "HOTEL_CHARGES", nullable = false)
    private Double hotelCharges;

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