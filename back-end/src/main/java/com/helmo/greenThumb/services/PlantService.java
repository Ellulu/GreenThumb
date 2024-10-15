package com.helmo.greenThumb.services;
import com.helmo.greenThumb.infrastructures.PlantRepository;
import com.helmo.greenThumb.model.Plant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    public Plant addPlant(Plant plant) {
        return plantRepository.save(plant);
    }

    public Optional<Plant> getPlantById(Long id) {
        return plantRepository.findById(id);
    }

    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }
}
