package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PETEL_FACILITIES")
public class FacilitiesEntity {

    /**
     * 設備ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 設備名稱
     */
    @Column(name = "NAME")
    private String name;
}
