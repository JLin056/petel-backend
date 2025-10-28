package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CHAT002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 每頁筆數
     */
    @JsonProperty("pageSize")
    private Integer pageSize;

    /**
     * 當前頁碼
     */
    @JsonProperty("pageNumber")
    private Integer pageNumber;

    /**
     * 總頁數
     */
    @JsonProperty("totalPage")
    private Integer totalPage;

    /**
     * 總筆數
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * 聊天室列表
     */
    @JsonProperty("chats")
    private List<CHAT002TranrsChats> chats;
}
