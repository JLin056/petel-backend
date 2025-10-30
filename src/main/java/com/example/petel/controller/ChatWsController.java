package com.example.petel.controller;

import com.example.petel.dto.ChatMessage;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.ChatMessageSvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatMessageSvc chatMessageSvc;

    /**
     * 傳送訊息
     * @param threadId
     * @param message
     * @param principal
     */
    @MessageMapping("/rooms/{threadId}/send")
    public void sendMessage(@DestinationVariable String threadId,
                            @Payload ChatMessage message,
                            Principal principal)
            throws InvalidInputException, JwtProcessingException {

        AccountPrincipal auth = (AccountPrincipal) principal;
        chatMessageSvc.handleIncomingMessage(threadId, message, auth);
    }

    /**
     * 標記已讀到 目前聊天室的最新一則訊息
     * @param threadId
     * @param principal
     * @throws InvalidInputException
     * @throws JwtProcessingException
     */
    @MessageMapping("/rooms/{threadId}/read")
    public void markReadLatest(@DestinationVariable String threadId,
                               Principal principal)
            throws InvalidInputException, JwtProcessingException {

        AccountPrincipal auth = (AccountPrincipal) principal;
        chatMessageSvc.handleReadEvent(threadId, null, auth);
    }

    /**
     * 標記已讀到指定訊息 ID
     * @param threadId
     * @param messageId
     * @param principal
     * @throws InvalidInputException
     * @throws JwtProcessingException
     */
    @MessageMapping("/rooms/{threadId}/read/{messageId}")
    public void markReadUpTo(@DestinationVariable String threadId,
                             @DestinationVariable String messageId,
                             Principal principal)
            throws InvalidInputException, JwtProcessingException {

        AccountPrincipal auth = (AccountPrincipal) principal;
        chatMessageSvc.handleReadEvent(threadId, messageId, auth);
    }
}
