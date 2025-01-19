package com.helmo.greenThumb.infrastructures;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInit {
    public FirebaseInit() {
        try (InputStream refreshToken = getClass().getClassLoader().getResourceAsStream("greenthumb-54c99-firebase-adminsdk-4l1mr-6b4f5c9b96.json")) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException ex) {
            System.out.println("FirebaseInit exception: " + ex);
        }
    }
}

