package com.example.petel.service.impl;

import com.example.petel.dto.ChatMessage;
import com.example.petel.entity.ChatMessagesEntity;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.repository.ChatMessagesRepository;
import com.example.petel.repository.ChatThreadRepository;
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
    /** ChatThreadRepository */
    private final ChatThreadRepository chatThreadRepo;
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

        if (sender == null || sender.getAccountId() == null) {
            throw new JwtProcessingException("未登入或身份無效");
        }
        if (threadId == null || threadId.isBlank()) {
            throw new InvalidInputException("threadId 不可為空");
        }
        if (msg == null) {
            throw new InvalidInputException("請提供訊息內容");
        }

        String content = safeTrim(msg.getContent());
        if (content == null || content.isBlank()) {
            log.warn("[Chat] 空訊息已忽略 threadId={}", threadId);
            return;
        }

        String type = normalizeType(msg.getType());
        if (type == null) {
            type = "TEXT";
        }

        String maxId = chatMessagesRepo.findMaxId();
        String messageId = IdUtil.generateTableId("M", maxId);

        ChatMessagesEntity entity = new ChatMessagesEntity();
        entity.setId(messageId);
        entity.setThreadId(threadId);
        entity.setSenderId(sender.getAccountId());
        entity.setMessageType(type);
        entity.setContent(content);
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
                if (!chatThreadRepo.existsMember(threadId, sender.getAccountId())) {
                    log.warn("[Chat] 非聊天室成員，略過推送：threadId={}, sender={}", threadId, sender.getAccountId());
                    return;
                }

                String peerId = chatThreadRepo.findPeerAccountId(threadId, sender.getAccountId())
                        .orElse(null);
                if (peerId == null) {
                    log.warn("[Chat] 查無對方帳號，略過推送 threadId={}", threadId);
                    return;
                }

                java.util.List<String> targets = java.util.List.of(sender.getAccountId(), peerId);
                for (String uid : targets) {
                    messagingTemplate.convertAndSendToUser(uid, "/queue/chat", saved);
                }
                log.info("[Chat] 已儲存並推送訊息 threadId={}, msgId={}, to={}", threadId, saved.getId(), targets);
            }
        });

    }

    /**
     * 去除空白
     * @param s
     * @return
     */
    private static String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }

    /**
     * 將 type 格式化成大寫
     * @param type
     * @return
     */
    private static String normalizeType(String type) {
        if (type == null || type.isBlank()) return null;
        return type.trim().toUpperCase();
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public void handleReadEvent(String threadId, String lastMessageId, AccountPrincipal reader)
            throws JwtProcessingException, InvalidInputException {

        // 驗證
        if (reader == null || reader.getAccountId() == null) {
            throw new JwtProcessingException("未登入或身份無效");
        }
        if (threadId == null || threadId.isBlank()) {
            throw new InvalidInputException("threadId 不可為空");
        }

        final String readerId = reader.getAccountId();
        if (!chatThreadRepo.existsMember(threadId, readerId)) {
            throw new JwtProcessingException("無權限存取此聊天室");
        }

        // 若未帶 lastMessageId，抓該 thread 最新一筆訊息
        String lastId = (lastMessageId == null || lastMessageId.isBlank())
                ? chatMessagesRepo.findLastMsgIdByThreadId(threadId)
                : lastMessageId;

        // 沒有任何訊息，不需要更新/推播
        if (lastId == null || lastId.isBlank()) return;

        // 更新 threads 的 last_read 欄位
        int updatedRows;
        if ("user".equalsIgnoreCase(reader.getRole())) {
            updatedRows = chatThreadRepo.updateLastReadMsgIdBuyer(threadId, lastId);
        } else {
            updatedRows = chatThreadRepo.updateLastReadMsgIdSeller(threadId, lastId);
        }

        if (updatedRows <= 0) {
            log.warn("[Chat] 已讀更新失敗 threadId={}, reader={}, lastMsg={}", threadId, readerId, lastId);
            return;
        }

        ChatMessage readEvent = ChatMessage.builder()
                .id(lastId)
                .threadId(threadId)
                .senderAccountId(readerId)     // 已讀者
                .type("READ")                  // type = READ
                .content(null)
                .createdAt(LocalDateTime.now(TPE))
                .build();

        // 更新後推播
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                String peerId = chatThreadRepo.findPeerAccountId(threadId, readerId).orElse(null);
                if (peerId == null) {
                    log.warn("[Chat] 查無對方帳號，略過 READ 推送 threadId={}", threadId);
                    return;
                }
                java.util.List<String> targets = java.util.List.of(readerId, peerId);
                for (String uid : targets) {
                    messagingTemplate.convertAndSendToUser(uid, "/queue/chat", readEvent);
                }
                log.info("[Chat] 已推送 READ 事件 threadId={}, lastMsg={}, to={}", threadId, lastId, targets);
            }
        });

    }
}
