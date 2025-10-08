package com.example.petel.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PETEL_ORDER_ITEMS")
public class PetelOrderItemsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @Column(name = "ARRIVAL_DATE", nullable = false)
    private String arrivalDate;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PRICE", nullable = false)
    private Double price;
}