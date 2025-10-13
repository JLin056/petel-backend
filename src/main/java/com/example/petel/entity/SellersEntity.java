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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELLERS_ID")
    @JsonAlias("sellersId")
    private Integer sellersId;

    /**
     * 帳號 ID (外鍵)
     */
    @Column(name = "ACCOUNTS_ID")
    @JsonAlias("accountsId")
    private Integer accountsId;

    /**
     * 賣家名稱
     */
    @Column(name = "SELLERS_NAME")
    @JsonAlias("sellersName")
    private String sellersName;

    /**
     * 統編
     */
    @Column(name = "BUSINESS_CODE")
    @JsonAlias("businessCode")
    private String businessCode;

    /**
     * 頭像媒體 ID
     */
    @Column(name = "AVATAR_MEDIA_ID")
    @JsonAlias("avatarMediaId")
    private Integer avatarMediaId;
}
