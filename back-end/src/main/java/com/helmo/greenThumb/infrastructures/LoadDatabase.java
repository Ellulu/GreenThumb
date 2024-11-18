package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RatingRepository ratingRepository, UserRepository userRepository, PlantRepository plantRepository,
                                   ArticleRepository articleRepository, EventRepository eventRepository,VarietyRepository varietyRepository) {
        return args -> {
            if (userRepository.count() > 0) {return;}
            // Créer des utilisateurs (User)
            Variety variety1 = new Variety();
            variety1.setName("Common");
            variety1.setDescription("CommonDescription");

            variety1 = varietyRepository.save(variety1);
            User user1 = new User();
            user1.setUid("A123ze45");
            user1 = userRepository.save(user1);

            User user2 = new User();
            user2.setUid("R678ty90");
            user2 = userRepository.save(user2);

            // Créer des plantes (Plant)
            Plant plant1 = new Plant("Ficus", 3.5,LightLevel.MEDIUM, variety1, new ArrayList<>(),user1);
            plant1 = plantRepository.save(plant1);

            Plant plant2 = new Plant("Aloe Vera", 2.0,LightLevel.HIGH, variety1, new ArrayList<>(),user2);
            plant2 = plantRepository.save(plant2);  // Sauvegarder la plante

            user1.setPlants(Arrays.asList(plant1));
            userRepository.save(user1);

            user2.setPlants(Arrays.asList(plant2));
            userRepository.save(user2);

            // Créer l'article et le sauvegarder avant de créer les ratings
            Article article1 = new Article();
            article1.setTitle("How to care for indoor plants");
            article1.setText("Indoor plants need indirect light and moderate watering.");
            article1.setDate(new Date());
            article1.setFiles(Arrays.asList("guide.pdf", "care-tips.txt"));
            article1.setAuthor(user1);
            article1 = articleRepository.save(article1);  // Sauvegarder l'article avant d'ajouter les ratings

            // Créer des ratings et les sauvegarder après
            Rating rating1 = new Rating(article1, user1, true);
            Rating rating2 = new Rating(article1, user2, false);
            ratingRepository.save(rating1);
            ratingRepository.save(rating2);

            // Mettre à jour les ratings dans l'article et sauvegarder à nouveau
            article1.setRatings(Set.of(rating1, rating2));
            articleRepository.save(article1);

            // Créer des événements (Event)
            Event event1 = new Event();
            event1.setDescription("Plant Workshop");
            event1.setEventDate(new Date());
            event1.setFiles(Arrays.asList("workshop.pdf"));
            event1.setAuthor(user1);
            eventRepository.save(event1);
        };

    }

}


