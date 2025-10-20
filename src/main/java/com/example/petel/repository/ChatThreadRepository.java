package com.example.petel.repository;

import com.example.petel.entity.ChatThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatThreadRepository extends JpaRepository<ChatThreadEntity, String> {

    /**
     * 用訂單 ID 查聊天室
     * @param orderId
     * @return
     */
    Optional<ChatThreadEntity> findByOrderId(String orderId);

    /**
     * 查目前最大
     * @return
     */
    @Query("select max(e.id) from ChatThreadEntity e")
    String findMaxId();
}
