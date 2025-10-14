package com.example.petel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_VIRTUAL_ACCOUNTS")
public class VirtualAccountsEntity {

    /**
     * 訂單編號
     */
    @Id // TODO 外鍵：PETEL_ORDERS
    @Column(name = "ORDER_ID", nullable = false)
    private String orderId;

    /**
     * 虛擬帳號到期日
     */
    @Column(name = "EXPIRED_AT", nullable = false)
    private String expiredAt;

    /**
     * 銀行代碼
     */
    @Column(name = "BANK_CODE", nullable = false)
    private String bankCode;

    /**
     * 虛擬帳號
     */
    @Column(name = "V_ACCOUNT", nullable = false)
    private String virtualAccount;
}