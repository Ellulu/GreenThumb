package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmo.greenThumb.model.Variety;
import com.helmo.greenThumb.services.VarietyService;
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
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(VarietyController.class)
class VarietyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VarietyService varietyService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllVarieties() throws Exception {
        Variety variety1 = new Variety("Varieté 1", "Groot");
        variety1.setId(1L);

        Variety variety2 = new Variety("Varieté 2", "Groot 2");
        variety2.setId(2L);

        List<Variety> varieties = Arrays.asList(variety1, variety2);

        Mockito.when(varietyService.getAllPlants()).thenReturn(varieties);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/varieties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].varietyName").value("Varieté 1"))
                .andExpect(jsonPath("$[0].description").value("Groot"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].varietyName").value("Varieté 2"))
                .andExpect(jsonPath("$[1].description").value("Groot 2"));
    }

    @Test
    void addVariety() throws Exception {
        Variety variety = new Variety("New Variety", "New Description");
        variety.setId(1L);

        Mockito.when(varietyService.addVariety(Mockito.any(Variety.class))).thenReturn(variety);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/varieties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variety)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.varietyName").value("New Variety"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    void getVarietyById() throws Exception {
        Variety variety = new Variety("Variety 1", "Description 1");
        variety.setId(1L);

        Mockito.when(varietyService.getVarietyById(1L)).thenReturn(Optional.of(variety));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/varieties/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.varietyName").value("Variety 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));
    }

    @Test
    void deleteVariety() throws Exception {
        Mockito.doNothing().when(varietyService).deleteVariety(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/varieties/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}