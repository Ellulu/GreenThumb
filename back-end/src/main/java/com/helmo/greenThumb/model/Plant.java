package com.helmo.greenThumb.model;

import jakarta.persistence.*;
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

    private String variety;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> taskList;

    public Plant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plant(String name, double monthlyWaterFrequency, String variety, List<Task> taskList) {
        this.name = name;
        this.monthlyWaterFrequency = monthlyWaterFrequency;
        this.variety = variety;
        this.taskList = taskList;
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

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
