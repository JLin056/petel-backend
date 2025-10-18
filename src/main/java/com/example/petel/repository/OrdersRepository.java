package com.example.petel.repository;

import com.example.petel.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, String> {

    /**
     * 查目前最大的表格 ID
     * @return ID
     */
    @Query("select max(e.id) from OrdersEntity e")
    String findMaxId();

    @Query(value = """
        SELECT o.USER_ID AS userId, p.SELLER_ID AS sellerId
        FROM PETEL_ORDERS o
        JOIN PETEL_PROPERTY p ON p.ID = o.PROPERTY_ID
        WHERE o.ID = :orderId
        """, nativeQuery = true)
    Map<String, Object> findUserAndSellerByOrderId(@Param("orderId") String orderId);
}