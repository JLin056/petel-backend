package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * PETEL_PROPERTY_IMAGE 表
 * 旅館圖片關聯表
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_PROPERTY_IMAGE")
@IdClass(PropertyImageEntity.PropertyImageId.class)
public class PropertyImageEntity {

    /**
     * 旅館ID
     */
    @Id
    @Column(name = "PROPERTY_ID", length = 10)
    private String propertyId;

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
    public static class PropertyImageId implements Serializable {
        private String propertyId;
        private String mediaId;
    }
}
