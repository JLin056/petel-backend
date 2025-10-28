package com.example.petel.service;

import com.example.petel.dto.ChatMessage;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.AccountPrincipal;

public interface ChatMessageSvc {

    void handleIncomingMessage(String threadId, ChatMessage msg, AccountPrincipal sender)
            throws JwtProcessingException, InvalidInputException;

    void handleReadEvent(String threadId, String lastMessageId, AccountPrincipal reader) throws JwtProcessingException, InvalidInputException;
}
