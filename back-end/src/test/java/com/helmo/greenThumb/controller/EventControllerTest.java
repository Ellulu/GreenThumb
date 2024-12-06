package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setDescription("Test Event");
        event.setEventDate(new Date());

        Mockito.when(eventService.createEvent(Mockito.any(Event.class))).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Event"));
    }

    @Test
    void getAllEvents() throws Exception {
       /* Event event1 = new Event();
        event1.setId(1L);
        event1.setDescription("Test Event 1");
        event1.setEventDate(new Date());

        Event event2 = new Event();
        event2.setId(2L);
        event2.setDescription("Test Event 2");
        event2.setEventDate(new Date());

        List<Event> events = Arrays.asList(event1, event2);

       // Mockito.when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Test Event 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].description").value("Test Event 2"));*/
    }

    @Test
    void getEventById() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setDescription("Test Event");
        event.setEventDate(new Date());

        Mockito.when(eventService.getEventById(1L)).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Event"));
    }

    @Test
    void deleteEvent() throws Exception {
        Mockito.doNothing().when(eventService).deleteEvent(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}