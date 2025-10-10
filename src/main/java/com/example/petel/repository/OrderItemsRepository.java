package com.example.petel.repository;

import com.example.petel.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Long> {

    /**
     * 查詢某訂單編號的詳細資料
     *
     * @param orderId Long
     * @return 某訂單編號對應的實體所構成的列表
     */
    List<OrderItemsEntity> findByOrderId(Long orderId);

    /**
     * 刪除某訂單編號的詳細資料
     *
     * @param orderId Long
     * @return 某訂單編號對應的實體所構成的列表
     */
    List<OrderItemsEntity> deleteByOrderId(Long orderId);
}