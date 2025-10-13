package com.example.petel.repository;

import com.example.petel.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    /**
     * 根據賣家 ID 查詢所有相關物業
     * @param sellerId 賣家 ID
     * @return 物業列表
     */
    List<PropertyEntity> findBySellerId(Integer sellerId);
}
