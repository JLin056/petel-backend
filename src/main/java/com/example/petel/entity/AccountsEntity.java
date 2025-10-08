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
@Table(name = "PETEL_ACCOUNTS")
public class AccountsEntity {

    /**
     * 帳號 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動遞增
    @Column(name = "ACCOUNT_ID")
    @JsonAlias("accountId")
    private Long accountId;

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

}
