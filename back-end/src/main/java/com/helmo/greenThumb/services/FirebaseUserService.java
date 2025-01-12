package com.helmo.greenThumb.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseUserService {
    public String getEmailFromUid(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord.getEmail(); // Récupère l'adresse e-mail de l'utilisateur
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer l'utilisateur Firebase : " + e.getMessage());
        }
    }
}
