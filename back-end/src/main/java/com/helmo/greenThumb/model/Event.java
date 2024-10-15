package com.helmo.greenThumb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;

    @ElementCollection
    private List<String> files;

    public void addEvent() {
        this.eventDate = new Date();
    }

    public Event getEvent() {
        return this;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
