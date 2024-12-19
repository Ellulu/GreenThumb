package com.helmo.greenThumb.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;


    private LocalDateTime notificationDate;

    private boolean isSent = false;

    public NotificationLog(LocalDateTime now, Event event) {
        this.notificationDate = now;
        this.event = event;
        this.isSent = false;
    }

    public NotificationLog() {

    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setNotificationDate(LocalDateTime now) {
        this.notificationDate = now;
    }

    public void setSent(boolean b) {
        this.isSent = b;
    }
    public LocalDate getNotificationDate() {
        return notificationDate.toLocalDate();
    }

    public boolean isSent() {
        return isSent;
    }

    public boolean timeMatch() {
        return notificationDate.toLocalDate().isEqual(LocalDate.now());
    }
}
