package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT003TranrsMessages implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * messageId
     */
    @JsonProperty("messageId")
    private String messageId;

    /**
     * senderId
     */
    @JsonProperty("senderId")
    private String senderId;

    /**
     * type
     */
    @JsonProperty("type")
    private String type;

    /**
     * content
     */
    @JsonProperty("content")
    private String content;

    /**
     * createdAt
     */
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}
