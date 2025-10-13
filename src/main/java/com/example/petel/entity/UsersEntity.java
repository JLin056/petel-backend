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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonAlias("usersId")
    private Integer usersId;

    /**
     * 帳號 ID (外鍵)
     */
    @Column(name = "ACCOUNTS_ID")
    @JsonAlias("accountsId")
    private Integer accountsId;

    /**
     * 姓名
     */
    @Column(name = "NAME")
    @JsonAlias("usersName")
    private String usersName;

    /**
     * 電話
     */
    @Column(name = "PHONE")
    @JsonAlias("usersPhone")
    private String usersPhone;

    /**
     * 頭像媒體 ID
     */
    @Column(name = "AVATAR_MEDIA_ID")
    @JsonAlias("avatarMediaId")
    private String avatarMediaId;
}
