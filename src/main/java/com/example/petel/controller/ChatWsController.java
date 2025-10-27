package com.example.petel.controller;

import com.example.petel.dto.ChatMessage;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.ChatMessageSvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
        if (!(principal instanceof AccountPrincipal auth)) {
            log.warn("[WS] 未登入用戶傳送訊息");
            return;
        }

        chatMessageSvc.handleIncomingMessage(threadId, message, auth);
    }
}
