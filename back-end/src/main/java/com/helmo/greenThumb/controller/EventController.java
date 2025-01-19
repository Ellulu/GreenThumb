package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.services.EventService;
import com.helmo.greenThumb.services.NotificationLogService;
import com.helmo.greenThumb.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.helmo.greenThumb.utils.EventUtils.isEventToday;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private  NotificationLogService notificationLogService;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(    @RequestAttribute("firebaseToken") FirebaseToken token,@RequestBody Event event) {

     Event createdevent =  eventService.createEvent(token.getUid(),event);

        if(isEventToday(createdevent)){
            notificationLogService.saveNotifForNewEvent(createdevent);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("l'event a bien été créé");
    }




@PostMapping("/get")
public ResponseEntity<List<Event>> getEvents(@RequestAttribute("firebaseToken") FirebaseToken token,
                                             @RequestBody Map<String, String> requestBody) {


   LocalDate start = LocalDate.parse(requestBody.get("startDate"));
    LocalDate end = LocalDate.parse(requestBody.get("endDate"));

        List<Event> events = eventService.getEventsFromDate(token.getUid(), start, end);
       return ResponseEntity.ok(events) ;

    }


    @PostMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents(@RequestAttribute("firebaseToken") FirebaseToken token) {
        List<Event> events = eventService.getAllEventsForUser( token.getUid());
        return ResponseEntity.ok(events);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editEvent( @PathVariable Long id,@RequestBody Event event) {
        eventService.editEvent( id,event);
        return ResponseEntity.status(HttpStatus.OK).body("L'event a bien été modifié");
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}