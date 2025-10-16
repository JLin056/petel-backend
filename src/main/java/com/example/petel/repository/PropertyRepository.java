package com.example.petel.repository;

import com.example.petel.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from PropertyEntity e")
    String findMaxId();
}
