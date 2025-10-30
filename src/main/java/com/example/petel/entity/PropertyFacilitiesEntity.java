package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_PROPERTY_FACILITIES")
public class PropertyFacilitiesEntity {

    /**
     * 旅館設備ID
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 旅館ID
     */
    @Column(name = "PROPERTY_ID")
    private String propertyId;

    /**
     * 設備ID
     */
    @Column(name = "FACILITY_ID")
    private String facilityId;
}
