package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import com.helmo.greenThumb.services.WebSocketSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SocketController {
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationLogService notificationLogService;
    private final WebSocketSessionManager sessionManager;
    public SocketController(SimpMessagingTemplate messagingTemplate, NotificationLogService notificationLogService, WebSocketSessionManager sessionManager) {
        this.messagingTemplate = messagingTemplate;
        this.notificationLogService = notificationLogService;
        this.sessionManager = sessionManager;
    }





    @Scheduled(fixedRate = 10000)
    public void sendNotifications() {
        List<NotificationLog> logs = notificationLogService.getAllNotificationLogs();

        for (NotificationLog log : logs) {
            String userId = log.getEvent().getUser().getUid();

            if (sessionManager.isUserConnected(userId)) { // Vérifie si l'utilisateur est connecté
                Map<String, String> notification = new HashMap<>();
                notification.put("title", "Nouvelle tache a effectuer");
                notification.put("body", "Message spécifique pour l'utilisateur");

                try {
                    messagingTemplate.convertAndSendToUser(
                            userId,
                            "/queue/notifications",
                            notification
                    );

                    log.setSent(true);
                    notificationLogService.save(log);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
                }
            }
        }}
}
