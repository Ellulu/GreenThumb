package com.helmo.greenThumb.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String taskName;
    private Date taskDate;
    private boolean notification;

    public Task(String taskName, Date taskDate, boolean notification) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.notification = notification;
    }


    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }


}
