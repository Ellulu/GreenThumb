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

            LocalDate eventLocalDate = evt.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(evt.getCycle() !=0){
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                long diffDays = java.time.temporal.ChronoUnit.DAYS.between(eventLocalDate, date);

                if (diffDays >= 0 && diffDays % evt.getCycle() == 0) {

                    Date formattedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    if(usage){


                    Event newEvent =new Event(cpt, evt.getTitle(), evt.getDescription(), evt.getCycle(), evt.getPlant(), evt.getUser(),formattedDate);
                    cpt++;



                    recurringEvents.add(newEvent);   }else {
                        recurringEvents.add(evt);
                    }
                }
            }
        }else{
                if (!eventLocalDate.isBefore(startDate) && !eventLocalDate.isAfter(endDate)){
                    recurringEvents.add(evt);
                }
            }
        }
        return recurringEvents;
    }

    public static boolean isEventToday(Event event) {

        LocalDate eventLocalDate = event.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        long diffDays = java.time.temporal.ChronoUnit.DAYS.between(eventLocalDate,   currentDate);


        return diffDays >= 0 && diffDays % event.getCycle() == 0;
    }
}