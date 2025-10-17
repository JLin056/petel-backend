package com.example.petel.repository;

import com.example.petel.entity.SellersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellersRepository extends JpaRepository<SellersEntity, String> {
    /**
     * 查目前最大的表格 ID
     *
     * @return ID
     */
    @Query("select max(e.id) from SellersEntity e")
    String findMaxId();
}
