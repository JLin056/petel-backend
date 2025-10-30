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
@Table(name = "PETEL_POSTALS")
public class PostalEntity {

    /**
     * 郵遞區號ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 縣市
     */
    @Column(name = "CITY")
    private String city;

    /**
     * 區域
     */
    @Column(name = "DISTRICT")
    private String district;
}