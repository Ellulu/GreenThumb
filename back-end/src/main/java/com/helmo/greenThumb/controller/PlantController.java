package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Plant;
import com.helmo.greenThumb.services.PlantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public List<Plant> getAllPlants(@RequestAttribute("firebaseToken") FirebaseToken token) {
        return plantService.getAllPlants(token.getUid());
    }


    @PostMapping
    public Plant addPlant(@RequestBody Plant plant) {
        return plantService.addOrUpdatePlant(plant);
    }

    @GetMapping("/{id}")
    public Plant getPlantById(@PathVariable Long id) {
        return plantService.getPlantById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));
    }

    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
    }
}
