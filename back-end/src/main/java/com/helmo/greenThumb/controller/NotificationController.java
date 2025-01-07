package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    @Autowired
    NotificationLogService notificationLogService;


    @PostMapping("/get")
    private ResponseEntity<List<NotificationLog>> getNotification(@RequestAttribute("firebaseToken") FirebaseToken token){
        List<NotificationLog>  notify = notificationLogService.findAllNotificationByUser(token.getUid());
        long count = 0;

        for (NotificationLog n : notify){
            Event event = n.getEvent();
            n.setEvent(new Event(count,event.getTitle(),event.getDescription(),event.getCycle(),event.getPlant(),event.getUser(),event.getEventDate()));
            count++;
        }
        return ResponseEntity.ok(notify);


    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteNotification(@PathVariable Long id){
        System.out.println("id = " + id);
        System.out.println("Suppressionde la notification");
        notificationLogService.delete(id);
        System.out.println("supprimé");
        return ResponseEntity.ok("La notification a bien été supprimée");
    }

}
