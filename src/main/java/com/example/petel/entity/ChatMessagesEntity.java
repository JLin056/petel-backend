package com.example.petel.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PETEL_CHAT_MESSAGES")
public class ChatMessagesEntity {

    /**
     * id
     */
    @Id
    @Column(name = "ID")
    @JsonAlias("id")
    private String id;

    /**
     * threadId
     */
    @Column(name = "THREAD_ID")
    @JsonAlias("threadId")
    private String threadId;

    /**
     * senderId
     */
    @Column(name = "SENDER_ID")
    @JsonAlias("senderId")
    private String senderId;

    /**
     * messageType
     */
    @Column(name = "MESSAGE_TYPE")
    @JsonAlias("messageType")
    private String messageType;

    /**
     * content
     */
    @Lob
    @Column(name = "CONTENT")
    @JsonAlias("content")
    private String content;

    /**
     * createAt
     */
    @Column(name = "CREATED_AT")
    @JsonAlias("createdAt")
    private LocalDateTime createdAt;
}
