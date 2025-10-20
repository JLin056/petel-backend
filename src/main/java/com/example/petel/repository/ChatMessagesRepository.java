package com.example.petel.repository;

import com.example.petel.entity.ChatMessagesEntity;
import com.example.petel.entity.ChatThreadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessagesEntity, String> {
}
