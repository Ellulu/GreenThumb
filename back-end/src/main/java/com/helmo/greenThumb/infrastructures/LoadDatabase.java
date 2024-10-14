package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Configuration
public class LoadDatabase {
    /*
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PlantRepository plantRepository,
                                   ArticleRepository articleRepository, EventRepository eventRepository) {

        return args -> {
            // Créer des utilisateurs (User)
            User user1 = new User();
            user1.setFirstName("Alice");
            user1.setLastName("Smith");
            user1.setEmail("alice@example.com");
            user1 = userRepository.save(user1);  // Sauvegarder l'utilisateur avant de lier des plantes

            User user2 = new User();
            user2.setFirstName("Bob");
            user2.setLastName("Jones");
            user2.setEmail("bob@example.com");
            user2 = userRepository.save(user2);  // Sauvegarder l'utilisateur avant de lier des plantes

            // Créer des plantes (Plant)
            Plant plant1 = new Plant("Ficus", 3.5, "Common Variety", new ArrayList<>());
            plant1 = plantRepository.save(plant1);  // Sauvegarder la plante

            Plant plant2 = new Plant("Aloe Vera", 2.0, "Common Variety", new ArrayList<>());
            plant2 = plantRepository.save(plant2);  // Sauvegarder la plante

            // Associer les plantes aux utilisateurs
            user1.setPlants(Arrays.asList(plant1));
            userRepository.save(user1);  // Sauvegarder les modifications de l'utilisateur

            user2.setPlants(Arrays.asList(plant2));
            userRepository.save(user2);  // Sauvegarder les modifications de l'utilisateur

            // Créer des articles (Article)
            Article article1 = new Article();
            article1.setTitle("How to care for indoor plants");
            article1.setText("Indoor plants need indirect light and moderate watering.");
            article1.setDate(new Date());
            article1.setFiles(Arrays.asList("guide.pdf", "care-tips.txt"));
            article1.setRating(new Rating(10, 2));
            articleRepository.save(article1);

            // Créer des événements (Event)
            Event event1 = new Event();
            event1.setDescription("Plant Workshop");
            event1.setEventDate(new Date());
            event1.setFiles(Arrays.asList("workshop.pdf"));
            eventRepository.save(event1);
        };
    }*/
}


