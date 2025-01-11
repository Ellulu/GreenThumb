package com.helmo.greenThumb.configs;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.services.WebSocketSessionManager;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {
    private final WebSocketSessionManager sessionManager;

    public WebSocketInterceptor(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            if ("CONNECT".equals(accessor.getCommand().name())) {
                try {
                    String token = accessor.getFirstNativeHeader("Authorization").replace("Bearer ", "");
                    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                    String uid = decodedToken.getUid();

                    accessor.setUser(() -> uid);
                    sessionManager.addUser(uid);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Token Firebase invalide ou manquant : " + e.getMessage());
                }
            } else if ("DISCONNECT".equals(accessor.getCommand().name())) {
                if (accessor.getUser() != null) {
                    String uid = accessor.getUser().getName();
                    sessionManager.removeUser(uid);
                }
            }
        }

        return message;
    }
}
