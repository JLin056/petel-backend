package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @JsonProperty("id")
    private String id;

    /**
     * threadId
     */
    @JsonProperty("threadId")
    private String threadId;

    /**
     * senderAccountId
     */
    @JsonProperty("senderAccountId")
    private String senderAccountId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
