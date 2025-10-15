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
@Table(name = "PETEL_USERS")
public class UsersEntity {

    /**
     * 使用者 ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 帳號 ID (外鍵)
     */
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 電話
     */
    @Column(name = "PHONE")
    private String phone;

    /**
     * 頭像媒體 ID
     */
    @Column(name = "AVATAR_MEDIA_ID")
    private String avatarMediaId;
}
