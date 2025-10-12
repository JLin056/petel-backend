package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "PETEL_REVIEWS")
public class ReviewEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PROPERTY_ID")
    private Long propertyId;

    @Column(name = "PRICE_SCORE")
    private Long priceScore;

    @Column(name = "ENV_SCORE")
    private Long envScore;

    @Column(name = "SERVICE_SCORE")
    private Long serviceScore;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;
}
