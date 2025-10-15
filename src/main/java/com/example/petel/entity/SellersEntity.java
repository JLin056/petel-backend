package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_SELLERS")
public class SellersEntity {

    /**
     * 賣家 ID
     */
    @Id
    @Column(name = "ID")
    @JsonAlias("Id")
    private String id;

    /**
     * 帳號 ID (外鍵)
     */
    @Column(name = "ACCOUNT_ID")
    @JsonAlias("accountId")
    private String accountId;

    /**
     * 賣家名稱
     */
    @Column(name = "NAME")
    @JsonAlias("name")
    private String name;

    /**
     * 統編
     */
    @Column(name = "BUSINESS_CODE")
    @JsonAlias("businessCode")
    private String businessCode;

    /**
     * 頭像媒體 ID
     */
    @Column(name ="AVATAR_MEDIA_ID")
    @JsonAlias("avatarMediaId")
    private Long avatarMediaId;
}
