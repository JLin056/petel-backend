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

    /**
     * 旅館ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 商家ID
     */
    @Column(name = "SELLER_ID")
    private String sellerId;

    /**
     * 旅館名稱
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 旅館電話
     */
    @Column(name = "TEL")
    private String tel;

    /**
     * 郵遞區號
     */
    @Column(name = "POSTAL_CODE")
    private String postalCode;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 收款帳號
     */
    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    /**
     * 旅館介紹
     */
    @Column(name = "INFO")
    private String info;

    /**
     * 入住須知
     */
    @Column(name = "CHECK_NOTICE")
    private String checkNotice;

    /**
     * 寵物入住注意事項
     */
    @Column(name = "PET_NOTICE")
    private String petNotice;

    /**
     * 旅館注意事項
     */
    @Column(name = "PROPERTY_NOTICE")
    private String propertyNotice;
}
