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
@Table(name = "PETEL_PROPERTY")
public class PropertyEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SELLER_ID")
    private Long sellerId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @Column(name = "NOTICE")
    private String notice;
}
