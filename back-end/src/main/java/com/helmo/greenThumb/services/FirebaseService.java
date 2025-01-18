package com.helmo.greenThumb.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.helmo.greenThumb.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FirebaseService {

    public FirebaseService(){

    }

    public UserRecord getUserByUid(String uid) {
        try {
            return FirebaseAuth.getInstance().getUser(uid);
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return null;
        }

    }
    public String getEmailFromUid(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord.getEmail(); // Récupère l'adresse e-mail de l'utilisateur
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer l'utilisateur Firebase : " + e.getMessage());
        }
    }
}
