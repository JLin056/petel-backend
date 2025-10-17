package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin002Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 賣家列表
     */
    @JsonProperty("sellers")
    private List<Admin002TranrsTranrs> sellers;

    /**
     * 總筆數
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * 總頁數
     */
    @JsonProperty("totalPages")
    private Integer totalPages;

    /**
     * 當前頁碼
     */
    @JsonProperty("currentPage")
    private Integer currentPage;
}
