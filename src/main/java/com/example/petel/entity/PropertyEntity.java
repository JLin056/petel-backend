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
    @Column(name = "PROPERTY_ID")
    private Long propertyId;

    @Column(name = "SELLER_ID")
    private Long sellerId;

    @Column(name = "PROPERTY_NAME")
    private String propertyName;

    @Column(name = "PROPERTY_TEL")
    private String propertyTel;

    @Column(name = "PROPERTY_POSTAL_CODE")
    private String propertyPostalCode;

    @Column(name = "PROPERTY_ADDRESS")
    private String propertyAddress;

    @Column(name = "PROPERTY_BANK_ACCOUNT")
    private String propertyBankAccount;

    @Column(name = "PROPERTY_NOTICE")
    private String propertyNotice;
}
