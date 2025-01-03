package com.helmo.greenThumb.configs;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && "CONNECT".equals(accessor.getCommand().name())) {
            try {
                String token = accessor.getFirstNativeHeader("Authorization").replace("Bearer ", "");

                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = decodedToken.getUid();

                accessor.setUser(() -> uid);
            } catch (Exception e) {
                throw new IllegalArgumentException("Token Firebase invalide ou manquant : " + e.getMessage());
            }
        }

        return message;
    }
}
