package com.example.petel.repository;

import com.example.petel.entity.ChatThreadEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 看是否存在在聊天室
     * @param threadId
     * @param accountId
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM ChatThreadsEntity t WHERE t.id = :threadId AND (t.userId = :accountId OR t.sellerId = :accountId)")
    boolean existsMember(@Param("threadId") String threadId, @Param("accountId") String accountId);

    /**
     * 找聊天室的成員
     * @param threadId
     * @param accountId
     * @return
     */
    @Query("SELECT CASE WHEN t.userId = :accountId THEN t.sellerId ELSE t.userId END " +
            "FROM ChatThreadsEntity t WHERE t.id = :threadId")
    Optional<String> findPeerAccountId(@Param("threadId") String threadId, @Param("accountId") String accountId);

    /**
     * 更新最後已讀訊息ID 買家
     * @param threadId
     * @param msgId
     * @return
     */
    @Modifying
    @Transactional
    @Query("UPDATE ChatThreadEntity t SET t.lastReadMsgIdBuyer = :msgId WHERE t.id = :threadId")
    int updateLastReadMsgIdBuyer(String threadId, String msgId);

    /**
     * 更新最後已讀訊息ID 賣家
     * @param threadId
     * @param msgId
     * @return
     */
    @Modifying
    @Transactional
    @Query("UPDATE ChatThreadEntity t SET t.lastReadMsgIdSeller = :msgId WHERE t.id = :threadId")
    int updateLastReadMsgIdSeller(String threadId, String msgId);
}
