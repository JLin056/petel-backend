package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
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
@Table(name = "PETEL_CHAT_THREADS")
public class ChatThreadEntity {

    /**
     * 聊天室 ID
     */
    @Id
    @Column(name = "ID")
    @JsonAlias("id")
    private String id;

    /**
     * orderId
     */
    @Column(name = "ORDER_ID")
    @JsonAlias("orderId")
    private String orderId;

    /**
     * userId
     */
    @Column(name = "USER_ID")
    @JsonAlias("userId")
    private String userId;

    /**
     * sellerId
     */
    @Column(name = "SELLER_ID")
    @JsonAlias("sellerId")
    private String sellerId;

    /**
     * status
     */
    @Column(name = "STATUS")
    @JsonAlias("status")
    private String status;

    /**
     * lastReadMsgIdBuyer
     */
    @Column(name = "LAST_READ_MSG_ID_BUYER")
    @JsonAlias("lastReadMsgIdBuyer")
    private String lastReadMsgIdBuyer;

    /**
     * lastReadMsgIdSeller
     */
    @Column(name = "LAST_READ_MSG_ID_SELLER")
    @JsonAlias("lastReadMsgIdSeller")
    private String lastReadMsgIdSeller;
}
