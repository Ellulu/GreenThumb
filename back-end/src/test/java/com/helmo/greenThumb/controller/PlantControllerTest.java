package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmo.greenThumb.model.Plant;
import com.helmo.greenThumb.services.PlantService;
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
@WebMvcTest(PlantController.class)
class PlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPlants() throws Exception {
        Plant plant1 = new Plant();
        plant1.setId(1L);
        plant1.setName("Plant 1");
        plant1.setMonthlyWaterFrequency(2.0);

        Plant plant2 = new Plant();
        plant2.setId(2L);
        plant2.setName("Plant 2");
        plant2.setMonthlyWaterFrequency(3.0);

        List<Plant> plants = Arrays.asList(plant1, plant2);

        Mockito.when(plantService.getAllPlants("")).thenReturn(plants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/plants")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Plant 1"))
                .andExpect(jsonPath("$[0].monthlyWaterFrequency").value(2.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Plant 2"))
                .andExpect(jsonPath("$[1].monthlyWaterFrequency").value(3.0));
    }

    @Test
    void addPlant() throws Exception {
        Plant plant = new Plant();
        plant.setId(1L);
        plant.setName("New Plant");
        plant.setMonthlyWaterFrequency(2.5);

        Mockito.when(plantService.addOrUpdatePlant(Mockito.any(Plant.class))).thenReturn(plant);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plant)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Plant"))
                .andExpect(jsonPath("$.monthlyWaterFrequency").value(2.5));
    }

    @Test
    void getPlantById() throws Exception {
        Plant plant = new Plant();
        plant.setId(1L);
        plant.setName("Plant 1");
        plant.setMonthlyWaterFrequency(2.0);

        Mockito.when(plantService.getPlantById(1L)).thenReturn(Optional.of(plant));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/plants/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Plant 1"))
                .andExpect(jsonPath("$.monthlyWaterFrequency").value(2.0));
    }

    @Test
    void deletePlant() throws Exception {
        Mockito.doNothing().when(plantService).deletePlant(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/plants/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}