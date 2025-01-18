package com.helmo.greenThumb.model;

import jakarta.persistence.*;

import java.util.Objects;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variety variety)) return false;
        return Objects.equals(name, variety.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
