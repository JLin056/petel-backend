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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "PROPERTY_ID", nullable = false)
    private Long propertyId;

    @Column(name = "PAYMENT_ID", nullable = false)
    private Integer paymentId;

    @Column(name = "HOTEL_CHARGES", nullable = false)
    private Double hotelCharges;

    @Column(name = "CHECK_IN", nullable = false)
    private String checkIn;

    @Column(name = "CHECK_OUT", nullable = false)
    private String checkOut;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATED_AT", nullable = false)
    private Timestamp createdAt;

    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
}