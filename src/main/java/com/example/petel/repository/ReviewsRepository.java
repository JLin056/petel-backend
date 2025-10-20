package com.example.petel.repository;

import com.example.petel.entity.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewsRepository extends JpaRepository<ReviewsEntity, String> {

    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from ReviewsEntity e")
    String findMaxId();

    /**
     * 檢查是否已有 orderId
     * @param orderId
     * @return
     */
    boolean existsByOrderId(String orderId);
}
