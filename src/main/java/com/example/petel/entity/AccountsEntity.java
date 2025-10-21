package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_ACCOUNTS")
public class AccountsEntity {

    /**
     * 帳號 ID
     */
    @Id
    @Column(name = "ID")
    @JsonAlias("Id")
    private String id;

    /**
     * 帳號（電子信箱）
     */
    @Column(name = "EMAIL")
    @JsonAlias("email")
    private String email;

    /**
     * 密碼
     */
    @Column(name = "PASSWORD")
    @JsonAlias("password")
    private String password;

    /**
     * 角色
     */
    @Column(name = "ROLE")
    @JsonAlias("role")
    private String role;

    /**
     * 帳號狀態
     */
    @Column(name = "STATUS")
    @JsonAlias("status")
    private String status;

    /**
     * Token 版本
     */
    @Column(name = "TOKEN_VERSION")
    @JsonAlias("tokenVersion")
    private int tokenVersion;
}
