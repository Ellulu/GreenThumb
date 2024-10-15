package com.helmo.greenThumb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.aspectj.weaver.ast.Var;
import org.springframework.data.annotation.Persistent;

import java.util.List;

@Entity
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double monthlyWaterFrequency;

    @Enumerated(EnumType.STRING)
    private LightLevel lightLevel;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Variety variety;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> taskList;

    @ElementCollection
    private List<String> files;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    public Plant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant(String name, double monthlyWaterFrequency, Variety variety, List<Task> taskList,User user) {
        this.name = name;
        this.monthlyWaterFrequency = monthlyWaterFrequency;
        this.variety = variety;
        this.taskList = taskList;
        this.owner = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlyWaterFrequency() {
        return monthlyWaterFrequency;
    }

    public void setMonthlyWaterFrequency(double monthlyWaterFrequency) {
        this.monthlyWaterFrequency = monthlyWaterFrequency;
    }

    public LightLevel getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(LightLevel lightLevel) {
        this.lightLevel = lightLevel;
    }

    public Variety getVariety() {
        return variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
