package com.helmo.greenThumb.controller;

import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.services.NotificationLogService;
import com.helmo.greenThumb.services.WebSocketSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ActiveProfiles("test")
@WebMvcTest(SocketController.class)
class SocketControllerTest {

    @Autowired
    private SocketController socketController;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @MockBean
    private NotificationLogService notificationLogService;

    @MockBean
    private WebSocketSessionManager sessionManager;

    private User user;
    private Event event;
    private NotificationLog log1;
    private NotificationLog log2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUid("testUser");

        event = new Event(1L, "Event Title", "Event Description", 0, null, null, new Date());
        event.setUser(user);

        log1 = new NotificationLog(LocalDateTime.now(), event);
        log2 = new NotificationLog(LocalDateTime.now(), event);

        Mockito.when(notificationLogService.getAllNotificationLogs()).thenReturn(Arrays.asList(log1, log2));
        Mockito.when(sessionManager.isUserConnected("testUser")).thenReturn(true);
    }

    @Test
    void sendNotifications() {
        socketController.sendNotifications();

        Mockito.verify(messagingTemplate, Mockito.times(2)).convertAndSendToUser(
                eq("testUser"),
                eq("/queue/notifications"),
                any(Map.class)
        );

        Mockito.verify(notificationLogService, Mockito.times(2)).save(any(NotificationLog.class));
    }
}