
package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.EventRepository;
import com.helmo.greenThumb.model.Event;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventNotificationService {

    private final EventRepository eventRepository;
    private final EventService eventService;
    private final NotificationService notificationService;


    public EventNotificationService(EventRepository eventRepository, NotificationService notificationService, EventService eventService) {
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
        this.eventService = eventService;
    }


    private void GetUpcomingEvents() {

        List<Event> recurringEvents = eventService.getAllEvents("zUaX99sOrsUNMrcx9SmU9YhJfXp2", LocalDate.now(), LocalDate.now());

        for (Event event : recurringEvents) {
            notificationService.notifyUser(event);


        }
    }
    @Scheduled(fixedRate = 60000) // Toutes les 60 secondes
    public void checkForUpcomingEvents() {
        System.out.println("on entre dans la boucle");
        /*
     GetUpcomingEvents();*/

    }
}