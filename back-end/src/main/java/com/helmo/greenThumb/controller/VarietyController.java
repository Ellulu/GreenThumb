package com.helmo.greenThumb.controller;

import com.helmo.greenThumb.model.Variety;
import com.helmo.greenThumb.services.VarietyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/varieties")
@CrossOrigin(origins = "http://localhost:5173")
public class VarietyController {

    private final VarietyService varietyService;

    public VarietyController(VarietyService varietyService) {
        this.varietyService = varietyService;
    }

    @GetMapping
    public List<Variety> getAllVarieties() {
        return varietyService.getAllPlants();
    }


    @PostMapping
    public Variety addVariety(@RequestBody Variety variety) {
        return varietyService.addVariety(variety);
    }

    @GetMapping("/{id}")
    public Variety getVarietyById(@PathVariable Long id) {
        return varietyService.getVarietyById(id)
                .orElseThrow(() -> new RuntimeException("Variety not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteVariety(@PathVariable Long id) {
        varietyService.deleteVariety(id);
    }
}
