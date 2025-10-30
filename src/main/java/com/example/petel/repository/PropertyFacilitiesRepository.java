package com.example.petel.repository;

import com.example.petel.entity.PropertyFacilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyFacilitiesRepository extends JpaRepository<PropertyFacilitiesEntity, String> {

    /**
     * 根據旅館ID查詢所有設備關聯
     * @param propertyId 旅館ID
     * @return 設備關聯列表
     */
    List<PropertyFacilitiesEntity> findByPropertyId(String propertyId);

    /**
     * 根據旅館ID刪除所有設備關聯
     * Spring Data JPA 會根據方法名稱自動生成實現
     * @param propertyId 旅館ID
     */
    void deleteByPropertyId(String propertyId);

    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from PropertyFacilitiesEntity e")
    String findMaxId();
}
