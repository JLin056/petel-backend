package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_USERS")
public class UsersEntity {

    /**
     * 使用者 ID
     */
    @Id
    @Column(name = "ID")
    @JsonProperty("id")
    private String id;

    /**
     * 帳號 ID (外鍵)
     */
    @Column(name = "ACCOUNT_ID")
    @JsonProperty("accountId")
    private String accountId;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    @JsonProperty("name")
    private String name;

    /**
     * 電話
     */
    @Column(name = "PHONE")
    @JsonProperty("phone")
    private String phone;

    /**
     * 頭像媒體 ID
     */
    @Column(name = "MEDIA_ID")
    @JsonProperty("mediaId")
    private String mediaId;
}
