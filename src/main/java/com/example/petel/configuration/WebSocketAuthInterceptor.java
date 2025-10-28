package com.example.petel.configuration;

import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Map<String, Object> attrs = accessor.getSessionAttributes();
            Object ap = (attrs != null) ? attrs.get("WS_PRINCIPAL") : null;

            if (ap instanceof AccountPrincipal principal) {
                accessor.setUser(principal);
                log.info("[WS] 使用握手建立的使用者：accountId={}", principal.getAccountId());
                return message;
            }

            log.warn("[WS] 握手為驗證，拒絕連線");
            return null;
        }

        return message;
    }
}
