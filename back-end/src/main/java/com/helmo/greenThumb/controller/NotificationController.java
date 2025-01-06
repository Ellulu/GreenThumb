package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.NotificationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    @Autowired
    NotificationLogService notificationLogService;




    @PostMapping("/get")
    private ResponseEntity<List<NotificationLog>> getNotification(@RequestAttribute("firebaseToken") FirebaseToken token){
        List<NotificationLog>  notify = notificationLogService.findAllNotificationByUser(token.getUid());

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
