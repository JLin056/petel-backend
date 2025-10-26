package com.example.petel.repository;

import com.example.petel.entity.ChatMessagesEntity;
import com.example.petel.entity.ChatThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessagesRepository extends JpaRepository<ChatMessagesEntity, String> {

    /**
     * 查目前最大 ID
     * @return
     */
    @Query("select max(e.id) from ChatMessagesEntity e")
    String findMaxId();
}
