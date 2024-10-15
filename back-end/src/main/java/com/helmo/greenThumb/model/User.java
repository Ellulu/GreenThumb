package com.helmo.greenThumb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.rmi.server.UID;
import java.util.List;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String uid;
    private String lastName;
    private String firstName;


    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @Lob
    private Byte[] profilePhoto; // Transformé en BLOB pour le stockage de la photo

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Plant> plants;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    private List<User> subscribers;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Event> events;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Article> articles;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

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
    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }
    public List<Plant> getPlants() {
        return plants;
    }

    public void followUser() {
        // Implémentation
    }

    public List<User> listSubscriptions() {
        return subscribers;
    }

    public void editAccount() {
        // Implémentation
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
