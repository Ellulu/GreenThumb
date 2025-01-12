package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationLogService notificationLogService;

    @MockBean
    private FirebaseToken firebaseToken;

    @BeforeEach
    void setUp() {
        Mockito.when(firebaseToken.getUid()).thenReturn("testUser");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getNotification() throws Exception {
        Event event = new Event(1L, "Event Title", "Event Description", 0, null, null, new Date());
        NotificationLog log1 = new NotificationLog(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), event);
        NotificationLog log2 = new NotificationLog(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(), event);

        List<NotificationLog> logs = Arrays.asList(log1, log2);

        Mockito.when(notificationLogService.findAllNotificationByUser(Mockito.anyString())).thenReturn(logs);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notifications/get")
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].event.title").value("Event Title"))
                .andExpect(jsonPath("$[1].event.title").value("Event Title"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void deleteNotification() throws Exception {
        Mockito.doNothing().when(notificationLogService).delete(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notifications/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("La notification a bien été supprimée"));
    }
}