package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.Plant;
import com.helmo.greenThumb.model.Variety;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PlantRepository plantRepository, VarietyRepository varietyRepository) {

        return args -> {
            Variety variety = new Variety();
            variety.setVarietyName("Ficus");
            variety.setDescription("Plante d'intérieur nécessitant peu d'entretien.");
            varietyRepository.save(variety);

            Plant plant = new Plant();
            plant.setName("Mon Ficus");
            plant.setMonthlyWaterFrequency(2);
            plant.setVariety(variety);  // Assurez-vous d'utiliser la variété sauvegardée
            plantRepository.save(plant);

            log.info("Preloaded Plant and Variety data.");
        };
    }
}
