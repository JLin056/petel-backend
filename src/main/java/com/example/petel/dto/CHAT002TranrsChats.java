package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CHAT002TranrsChats implements Serializable {

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
     * 顯示名稱（買家：旅館名稱、賣家：使用者名稱）
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * 最後一封訊息
     */
    @JsonProperty("lastMessage")
    private String lastMessage;

    /**
     * 最後一封訊息時間
     */
    @JsonProperty("lastMessageTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private LocalDateTime lastMessageTime;
}
