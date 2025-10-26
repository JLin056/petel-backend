package com.example.petel.repository;

import com.example.petel.entity.PropertyImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 旅館圖片關聯 Repository
 */
@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImageEntity, PropertyImageEntity.PropertyImageId> {

    /**
     * 根據旅館ID查詢所有圖片
     * @param propertyId 旅館ID
     * @return 圖片列表
     */
    List<PropertyImageEntity> findByPropertyId(String propertyId);

    /**
     * 根據媒體ID查詢
     * @param mediaId 媒體ID
     * @return 圖片列表
     */
    List<PropertyImageEntity> findByMediaId(String mediaId);

    /**
     * 根據旅館ID查詢最大排序順序
     * @param propertyId 旅館ID
     * @return 最大排序順序
     */
    @Query("SELECT COALESCE(MAX(p.sortOrder), 0) FROM PropertyImageEntity p WHERE p.propertyId = :propertyId")
    Integer findMaxSortOrderByPropertyId(String propertyId);
}
