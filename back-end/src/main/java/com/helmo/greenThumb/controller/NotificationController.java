package com.helmo.greenThumb.controller;

import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationLogService notificationLogService;

    public NotificationController(SimpMessagingTemplate messagingTemplate, NotificationLogService notificationLogService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationLogService = notificationLogService;
    }

    @Scheduled(fixedRate = 10000)
    public void sendNotifications() {
        List<NotificationLog> logs = notificationLogService.getAllNotificationLogs();

        for (NotificationLog log : logs) {
            Map<String, String> notification = new HashMap<>();
            notification.put("title", "Nouvelle notification !");
            notification.put("body", "Message spécifique pour l'utilisateur");

            try {
                messagingTemplate.convertAndSendToUser(
                        log.getEvent().getUser().getUid(),
                        "/queue/notifications",
                        notification
                );


                log.setSent(true);
                notificationLogService.save(log);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
            }
        }
    }
}
