package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.UserRepository;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.infrastructures.EventRepository;
import com.helmo.greenThumb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public Event createEvent(Event event) {



        return   eventRepository.save(event);
    }

    public List<Event> getAllEvents(String userUid) {
        User findUser = userRepository.findById(userUid).orElse(null);

        if (findUser == null) {
            return Collections.emptyList();
        }
        return eventRepository.findByUser(findUser);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
