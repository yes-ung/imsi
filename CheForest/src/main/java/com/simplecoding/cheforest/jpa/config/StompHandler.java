package com.simplecoding.cheforest.jpa.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // accessor.getUser()는 HttpSessionHandshakeInterceptor를 통해 설정된 Principal 객체입니다.
            // 이 객체가 존재한다면, Handshake 단계에서 인증 정보가 성공적으로 넘어온 것입니다.
            Optional<Principal> userOptional = Optional.ofNullable(accessor.getUser());

            if (userOptional.isPresent()) {
                Authentication auth = (Authentication) userOptional.get();
                // STOMP 세션에 사용자 정보를 설정합니다.
                accessor.setUser(auth);
                log.info("STOMP CONNECT - User authenticated and set in session: {}", auth.getName());
            } else {
                // 만약 HandshakeInterceptor를 통해 넘어온 정보가 없다면,
                // SecurityContextHolder에서 직접 가져오는 것을 시도합니다.
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated()) {
                    accessor.setUser(auth);
                    log.info("STOMP CONNECT - User found in SecurityContextHolder and set in session: {}", auth.getName());
                } else {
                    log.warn("STOMP CONNECT - User not authenticated.");
                }
            }
        }
        return message;
    }
}

