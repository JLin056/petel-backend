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
public class HOTEL005TranrsRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 房型名稱
     */
    @JsonProperty("name")
    private String name;

    /**
     * 房型資訊
     */
    @JsonProperty("info")
    private String info;

    /**
     * 房型面積
     */
    @JsonProperty("roomSize")
    private String roomSize;

    /**
     * 基本價格
     */
    @JsonProperty("basePrice")
    private Integer basePrice;

    /**
     * 總房數 / 可用數量
     */
    @JsonProperty("totalUnits")
    private Integer totalUnits;

    /**
     * 房型圖片列表
     */
    @JsonProperty("roomImages")
    private List<HOTEL005TranrsImage> roomImages;
}
