package com.example.petel.repository;

import com.example.petel.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * 用 orderId 取 userId 和 PropertyId
     * @param orderId
     * @return
     */
    @Query(value = "SELECT USER_ID AS USERID, PROPERTY_ID AS PROPERTYID FROM PETEL_ORDERS WHERE ID = :orderId", nativeQuery = true)
    Map<String, Object> findUserAndPropertyByOrderId(@Param("orderId") String orderId);

    /**
     * 用 userId 查 order
     *
     * @param userId
     * @return
     */
    @Query(value = """
        SELECT *
        FROM PETEL_ORDERS
        WHERE USER_ID = :userId
        ORDER BY CREATED_AT DESC
    """, nativeQuery = true)
    List<OrdersEntity> findByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);

}