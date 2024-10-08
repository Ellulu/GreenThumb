package com.helmo.greenThumb.model;

import java.util.List;

public class User {
    private String email;
    private String lastName;
    private String firstName;
    private String profilePhoto;

    private List<Plant> plants;

    public void createProfile() {
        // Implémentation
    }

    public boolean authenticate() {
        // Implémentation
        return true;
    }

    public void addPlant() {
        // Implémentation
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void followUser() {
        // Implémentation
    }

    public List<User> listSubscriptions() {
        // Implémentation
        return null;
    }

    public void editAccount() {
        // Implémentation
    }
}

