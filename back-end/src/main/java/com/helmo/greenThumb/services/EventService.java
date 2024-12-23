package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.UserRepository;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.infrastructures.EventRepository;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public Event createEvent(String uid,Event event) {

        User findUser = userRepository.findById(uid).orElse
                (null);
        event.setUser(findUser);
        return   eventRepository.save(event);
    }

    /*
    public List<Event> getAllEvents(String userUid ) {
        User findUser = userRepository.findById(userUid).orElse(null);

        if (findUser == null) {
            return Collections.emptyList();
        }
        List<Event> events = eventRepository.findAllByUser(findUser);
        EventUtils.calculateRecurringEvents(events,) ;
        return ;
    }*/

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event>  getAllEventForBack(){
        User findUser = userRepository.findById("zUaX99sOrsUNMrcx9SmU9YhJfXp2").orElse(null);

        if (findUser == null) {
            return Collections.emptyList();
        }
        List<Event> events = eventRepository.findByUser(findUser);

        return    EventUtils.calculateRecurringEvents(events, LocalDate.now(),LocalDate.now(),false) ;
    }

    public List<Event> getAllEvents(String userId, LocalDate startDate, LocalDate endDate) {
        User findUser = userRepository.findById(userId).orElse(null);

        if (findUser == null) {
            return Collections.emptyList();
        }
        List<Event> events = eventRepository.findByUser(findUser);

        return    EventUtils.calculateRecurringEvents(events,startDate,endDate,true) ;
    }


}
