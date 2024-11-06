package com.helmo.greenThumb.model;

import jakarta.persistence.*;

@Entity
public class Variety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    public Variety(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Variety() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVarietyName() {
        return name;
    }

    public void setVarietyName(String varietyName) {
        this.name = varietyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
