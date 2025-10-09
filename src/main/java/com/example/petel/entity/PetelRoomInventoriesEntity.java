package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_ROOM_INVENTORIES")
public class PetelRoomInventoriesEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房型編號
     */
    @Column(name = "PRODUCT_ID")
    private Long productId;

    /**
     * 入住日期
     */
    @Column(name = "STAY_DATE")
    private String stayDate;

    /**
     * 該房型數量
     */
    @Column(name = "TOTAL_UNITS")
    private Integer totalUnits;

    /**
     * 該房型可供販賣的數量
     */
    @Column(name = "AVAILABLE_QTY")
    private Integer availableQuantity;

    /**
     * 該房型售價
     */
    @Column(name = "PRICE")
    private Integer price;
}