package com.example.petel.repository;

import com.example.petel.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from PropertyEntity e")
    String findMaxId();
}

    /**
     * 根據賣家 ID 查詢所有相關物業
     * @param sellerId 賣家 ID
     * @return 物業列表
     */
    List<PropertyEntity> findBySellerId(String sellerId);
}