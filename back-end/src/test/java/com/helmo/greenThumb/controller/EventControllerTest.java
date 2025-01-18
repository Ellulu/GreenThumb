package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.services.EventService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

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
    void getEvents() throws Exception {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setEventDate(new Date());

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setEventDate(new Date());

        List<Event> events = Arrays.asList(event1, event2);

        Mockito.when(eventService.getEventsFromDate(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("startDate", "2023-01-01", "endDate", "2023-12-31")))
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getAllEvents() throws Exception {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        event1.setEventDate(new Date());

        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        event2.setEventDate(new Date());

        List<Event> events = Arrays.asList(event1, event2);

        Mockito.when(eventService.getAllEventsForUser(Mockito.anyString())).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events/all")
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void editEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Updated Event");

        Mockito.doNothing().when(eventService).editEvent(Mockito.anyLong(), Mockito.any(Event.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("L'event a bien été modifié"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void deleteEvent() throws Exception {
        Mockito.doNothing().when(eventService).deleteEvent(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/events/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
