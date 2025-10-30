package com.example.petel.repository;

import com.example.petel.entity.ChatMessagesEntity;
import com.example.petel.entity.ChatThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessagesRepository extends JpaRepository<ChatMessagesEntity, String> {

    /**
     * 查目前最大 ID
     * @return
     */
    @Query("select max(e.id) from ChatMessagesEntity e")
    String findMaxId();

    /**
     * 找最後已讀訊息
     * @param threadId
     * @return
     */
    @Query("SELECT e.id FROM ChatMessagesEntity e " +
            "WHERE e.threadId = :threadId " +
            "AND e.createdAt = (" +
            "  SELECT MAX(m.createdAt) FROM ChatMessagesEntity m WHERE m.threadId = :threadId" +
            ")")
    String findLastMsgIdByThreadId(String threadId);
}
