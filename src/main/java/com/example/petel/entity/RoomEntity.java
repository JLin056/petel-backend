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
@Table(name = "PETEL_ROOM")
public class RoomEntity {

    /**
     * 房型編號
     */
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 旅館編號
     */
    @Column(name = "PROPERTY_ID")
    private Long propertyId;

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
    private Integer petTypeId;
}