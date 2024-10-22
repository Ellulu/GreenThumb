package com.helmo.greenThumb.services;
import com.helmo.greenThumb.infrastructures.VarietyRepository;
import com.helmo.greenThumb.model.Variety;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VarietyService {

    private final VarietyRepository varietyRepository;

    public VarietyService(VarietyRepository varietyRepository) {
        this.varietyRepository = varietyRepository;
    }

    public List<Variety> getAllPlants() {
        return varietyRepository.findAll();
    }

    public Variety addVariety(Variety plant) {
        return varietyRepository.save(plant);
    }

    public Optional<Variety> getVarietyById(Long id) {
        return varietyRepository.findById(id);
    }

    public void deleteVariety(Long id) {
        varietyRepository.deleteById(id);
    }
}
