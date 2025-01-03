package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.services.EventService;
import com.helmo.greenThumb.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PlantService plantService;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(    @RequestAttribute("firebaseToken") FirebaseToken token,@RequestBody Event event) {

      eventService.createEvent(token.getUid(),event);
        return ResponseEntity.status(HttpStatus.CREATED).body("l'event a bien été créé");
    }
/**
    @GetMapping("/{uid}")
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable("uid") String userUid) {
        List<Event> events = eventService.getAllEvents(userUid);
        return ResponseEntity.ok(events) ;
    }
    **/
@PostMapping("/get")
public ResponseEntity<List<Event>> getEvents(@RequestAttribute("firebaseToken") FirebaseToken token,
                                             @RequestBody Map<String, String> requestBody) {


   LocalDate start = LocalDate.parse(requestBody.get("startDate"));
    LocalDate end = LocalDate.parse(requestBody.get("endDate"));

        List<Event> events = eventService.getEventsFromDate(token.getUid(), start, end);
        System.out.println(events.size());
        System.out.println(events);
       return ResponseEntity.ok(events) ;

    }


    @PostMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents(@RequestAttribute("firebaseToken") FirebaseToken token) {
        List<Event> events = eventService.getAllEventsForUser( token.getUid());
        return ResponseEntity.ok(events);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editEvent( @PathVariable Long id,@RequestBody Event event) {
    System.out.println("Requête bien reçue");
        eventService.editEvent( id,event);
        return ResponseEntity.status(HttpStatus.OK).body("L'event a bien été modifié");
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}