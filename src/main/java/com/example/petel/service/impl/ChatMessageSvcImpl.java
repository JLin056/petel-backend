package com.example.petel.service.impl;

import com.example.petel.dto.ChatMessage;
import com.example.petel.entity.ChatMessagesEntity;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.repository.ChatMessagesRepository;
import com.example.petel.service.ChatMessageSvc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageSvcImpl implements ChatMessageSvc {

    /** ChatMessagesRepository */
    private final ChatMessagesRepository chatMessagesRepo;
    /** SimpMessagingTemplate */
    private final SimpMessagingTemplate messagingTemplate;
    /** ZoneId TPE */
    private static final ZoneId TPE = ZoneId.of("Asia/Taipei");

    /**
     * 處理訊息並存進 DB
     * @param threadId
     * @param msg
     * @param sender
     * @throws JwtProcessingException
     * @throws InvalidInputException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void handleIncomingMessage(String threadId, ChatMessage msg, AccountPrincipal sender)
            throws JwtProcessingException, InvalidInputException {
        log.info("---- [ChatMessage] 處理訊息並存進 DB ----");

        if (sender == null) {
            throw new JwtProcessingException("未登入或身份無效");
        }
        if (threadId == null || threadId.isBlank()) {
            throw new InvalidInputException("threadId 不可為空");
        }
        if (msg == null || msg.getContent() == null || msg.getContent().isBlank()) {
            log.warn("[Chat] 空訊息已忽略 threadId={}", threadId);
            return;
        }

        msg.setSenderAccountId(sender.getAccountId());

        String maxId = chatMessagesRepo.findMaxId();
        String messageId = IdUtil.generateTableId("M", maxId);

        ChatMessagesEntity entity = new ChatMessagesEntity();
        entity.setId(messageId);
        entity.setThreadId(threadId);
        entity.setSenderId(msg.getSenderAccountId());
        entity.setMessageType(msg.getType());
        entity.setContent(msg.getContent());
        entity.setCreatedAt(LocalDateTime.now(TPE));

        chatMessagesRepo.save(entity);

        ChatMessage saved = ChatMessage.builder()
                .id(entity.getId())
                .threadId(entity.getThreadId())
                .senderAccountId(entity.getSenderId())
                .type(entity.getMessageType())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();

        // 存進 DB 後，再廣播
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                messagingTemplate.convertAndSend("/topic/room." + threadId, saved);
                log.info("[Chat] 已儲存並廣播訊息：{}", saved);
            }
        });
    }
}
