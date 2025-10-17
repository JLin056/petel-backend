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
@Table(name = "PETEL_ROOMS")
public class RoomsEntity {

    /**
     * 房型編號
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 旅館編號
     */
    @Column(name = "PROPERTY_ID", nullable = false)
    private String propertyId;

    /**
     * Name
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 房型數量
     */
    @Column(name = "TOTAL_UNITS")
    private Integer totalUnits;

    /**
     * 房型價格
     */
    @Column(name = "BASE_PRICE")
    private Integer basePrice;

    /**
     * 寵物類型
     */
    @Column(name = "PET_TYPE_ID")
    private String petTypeId;

    /**
     * 房型資訊
     */
    @Column(name = "INFO")
    private String info;

    /**
     * 房型面積
     */
    @Column(name = "ROOM_SIZE")
    private String roomSize;
}