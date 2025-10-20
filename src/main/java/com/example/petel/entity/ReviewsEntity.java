package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_REVIEWS")
public class ReviewsEntity {

    /**
     * id
     */
    @Id
    @Column(name = "ID")
    @JsonAlias("Id")
    private String id;

    /**
     * orderId
     */
    @Column(name = "ORDER_ID")
    @JsonAlias("orderId")
    private String orderId;

    /**
     * userId
     */
    @Column(name = "USER_ID")
    @JsonAlias("userId")
    private String userId;

    /**
     * propertyId
     */
    @Column(name = "PROPERTY_ID")
    @JsonAlias("propertyId")
    private String propertyId;

    /**
     * priceScore
     */
    @Column(name = "PRICE_SCORE")
    @JsonAlias("priceScore")
    private Integer priceScore;

    /**
     * envScore
     */
    @Column(name = "ENV_SCORE")
    @JsonAlias("envScore")
    private Integer envScore;

    /**
     * serviceScore
     */
    @Column(name = "SERVICE_SCORE")
    @JsonAlias("serviceScore")
    private Integer serviceScore;

    /**
     * content
     */
    @Column(name = "CONTENT")
    @JsonAlias("content")
    private String content;

    /**
     * createAt
     */
    @Column(name = "CREATED_AT")
    @JsonAlias("createAt")
    private LocalDateTime createAt;
}
