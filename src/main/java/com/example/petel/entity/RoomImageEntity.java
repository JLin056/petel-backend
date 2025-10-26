package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * PETEL_ROOM_IMAGE 表
 * 房型圖片關聯表
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_ROOM_IMAGE")
@IdClass(RoomImageEntity.RoomImageId.class)
public class RoomImageEntity {

    /**
     * 房型ID
     */
    @Id
    @Column(name = "ROOM_ID", length = 10)
    private String roomId;

    /**
     * 媒體ID
     */
    @Id
    @Column(name = "MEDIA_ID", length = 10)
    private String mediaId;

    /**
     * 排序順序
     */
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;

    /**
     * 複合主鍵類別
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomImageId implements Serializable {
        private String roomId;
        private String mediaId;
    }
}
