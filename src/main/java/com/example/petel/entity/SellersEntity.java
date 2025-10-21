package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "PETEL_SELLERS")
@Data
public class SellersEntity {

    /**
     * 商家ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 帳號ID
     */
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    /**
     * 商家姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 營業編號
     */
    @Column(name = "BUSINESS_CODE")
    private String businessCode;

    /**
     * 大頭照圖片ID
     */
    @Column(name = "MEDIA_ID")
    private String mediaId;
}
