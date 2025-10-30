package com.example.petel.repository;

import com.example.petel.entity.FacilitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilitiesRepository extends JpaRepository<FacilitiesEntity, String> {
    /**
     * 查詢所有設備
     * @return 設備列表
     */
    List<FacilitiesEntity> findAll();
}
