package com.example.petel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * MEDIA-004 Base64 圖片查詢 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MEDIA004Tranrs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 查詢到的媒體總數
     */
    @JsonProperty("totalCount")
    private Integer totalCount;

    /**
     * 媒體查詢結果列表
     */
    @JsonProperty("medias")
    private List<MEDIA004TranrsMediaInfo> medias;
}
