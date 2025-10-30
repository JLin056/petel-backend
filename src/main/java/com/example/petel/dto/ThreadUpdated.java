package com.example.petel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThreadUpdated {

    /**
     * threadId
     */
    private String threadId;

    /**
     * lastMessage
     */
    private String lastMessage;

    /**
     * lastMessageTime
     */
    private LocalDateTime lastMessageTime;

    /**
     * senderId
     */
    private String senderId;
}
