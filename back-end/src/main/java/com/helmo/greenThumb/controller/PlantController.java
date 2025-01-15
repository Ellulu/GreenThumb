package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.StorageClient;
import com.helmo.greenThumb.dto.PlantDTO;
import com.helmo.greenThumb.model.LightLevel;
import com.helmo.greenThumb.model.Plant;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.model.Variety;
import com.helmo.greenThumb.services.PlantService;
import com.helmo.greenThumb.services.VarietyService;
import com.helmo.greenThumb.utils.FileValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    private final PlantService plantService;
    private final VarietyService varietyService;
    public PlantController(PlantService plantService,VarietyService varietyService) {
        this.plantService = plantService;
        this.varietyService = varietyService;

    }

    @GetMapping
    public List<Plant> getAllPlants(@RequestAttribute("firebaseToken") FirebaseToken token) {
        return plantService.getAllPlants(token.getUid());
    }

    @PostMapping("/add")
    public Plant addPlant(
            @RequestAttribute("firebaseToken") FirebaseToken token,
            @RequestParam("plant") String plantJson,
            @RequestParam("picture") MultipartFile picture
    ) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Plant plant = objectMapper.readValue(plantJson, Plant.class);
            User plantOwner = new User();
            plantOwner.setUid(token.getUid());
            plant.setOwner(plantOwner);
            // Gestion de l'upload de l'image
            String pictureUrl = null;
            if (picture != null) {
                if (FileValidator.validateImage(picture)) {
                    Bucket bucket = StorageClient.getInstance().bucket("greenthumb-54c99.firebasestorage.app");
                    String blobName = "plants/" + token.getUid() + "/" + new Date().getTime() + "-" + picture.getOriginalFilename();
                    Blob blob = bucket.create(blobName, picture.getInputStream());

                    pictureUrl = "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();
                }
            }
            plant.setPicture(pictureUrl);
            return plantService.addOrUpdatePlant(plant);

        } catch (Exception e) {
            System.out.println(">>> Erreur dans la méthode addPlant: " + e.getMessage());
            e.printStackTrace();
        }
        return new Plant();
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
