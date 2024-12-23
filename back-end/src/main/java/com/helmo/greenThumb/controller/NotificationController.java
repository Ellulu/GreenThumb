package com.helmo.greenThumb.controller;

import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationLogService notificationLogService;

    public NotificationController(SimpMessagingTemplate messagingTemplate, NotificationLogService notificationLogService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationLogService = notificationLogService;
    }
/*
    @Scheduled(fixedRate = 10000)
    public void sendNotifications() {
        List<NotificationLog> logs = notificationLogService.getAllNotificationLogs();
        messagingTemplate.convertAndSend("/topic/notifications", "test");
        for (NotificationLog log : logs) {

            log.setSent(true);
            notificationLogService.save(log); // Marquer comme envoyé
        }
    }*/
}
