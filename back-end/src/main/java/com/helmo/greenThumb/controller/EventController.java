package com.helmo.greenThumb.controller;

import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.services.EventService;
import com.helmo.greenThumb.services.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PlantService plantService;

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody Event event) {

      eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("l'event a bien été créé");
    }

    @GetMapping("/{uid}")
    public ResponseEntity<List<Event>> getUserEvents(@PathVariable("uid") String userUid) {
        List<Event> events = eventService.getAllEvents(userUid);
        return ResponseEntity.ok(events) ;
    }

  /*  @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }*/

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}