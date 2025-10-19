package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT001Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * threadId
     */
    @JsonProperty("threadId")
    @JsonAlias("id")
    private String threadId;

    /**
     * orderId
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * userId
     */
    @JsonProperty("userId")
    private String userId;

    /**
     * sellerId
     */
    @JsonProperty("sellerId")
    private String sellerId;

    /**
     * status
     */
    @JsonProperty("status")
    private String status;

    /**
     * topic
     */
    @JsonProperty("topic")
    private String topic;
}
