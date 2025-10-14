package com.example.petel.repository;

import com.example.petel.entity.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, String> {

    /**
     * 查詢某訂單編號的詳細資料
     *
     * @param orderId String
     * @return 某訂單編號對應的實體所構成的列表
     */
    List<OrderItemsEntity> findByOrderId(String orderId);

    /**
     * 刪除某訂單編號的詳細資料
     *
     * @param orderId String
     * @return 某訂單編號對應的實體所構成的列表
     */
    List<OrderItemsEntity> deleteByOrderId(String orderId);

    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from OrderItemsEntity e")
    String findMaxId();
}