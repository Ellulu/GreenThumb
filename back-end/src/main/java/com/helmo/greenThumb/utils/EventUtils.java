package com.helmo.greenThumb.utils;

import com.helmo.greenThumb.model.Event;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EventUtils {


    public static List<Event> calculateRecurringEvents(List<Event> events, LocalDate startDate, LocalDate endDate,boolean usage) {
        List<Event> recurringEvents = new ArrayList<>();
        long cpt = 0;
        for (Event evt : events) {
            // Conversion de la date d'événement en LocalDate
            LocalDate eventLocalDate = evt.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                long diffDays = java.time.temporal.ChronoUnit.DAYS.between(eventLocalDate, date);

                if (diffDays >= 0 && diffDays % evt.getCycle() == 0) {
                    // Créer une copie de l'événement


                    // Convertir LocalDate en Date sans problème de fuseau horaire
                    Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    if(usage){


                    Event newEvent =new Event(cpt, evt.getTitle(), evt.getDescription(), evt.getCycle(), evt.getPlant(), evt.getUser(),formattedDate);
                    cpt++;

                    // Affichage formaté pour vérifier
                    System.out.println("Event title: " + newEvent.getTitle());
                    System.out.println("Event date: " + newEvent.getEventDate());

                    recurringEvents.add(newEvent);   }else {
                        recurringEvents.add(evt);
                    }
                }
            }
        }

        return recurringEvents;
    }
}