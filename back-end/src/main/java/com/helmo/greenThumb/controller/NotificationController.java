package com.helmo.greenThumb.controller;

import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import com.helmo.greenThumb.services.EmailService;
import com.helmo.greenThumb.services.FirebaseUserService;
import com.helmo.greenThumb.services.NotificationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    @Autowired
    NotificationLogService notificationLogService;
    @Autowired
    FirebaseUserService firebaseUserService;
    @Autowired
    private EmailService emailService;

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
        notificationLogService.delete(id);
        return ResponseEntity.ok("La notification a bien été supprimée");
    }
     @Scheduled(fixedRate = 3600000)
    public void SendMail(){
       Map<String ,List<NotificationLog>> logs = notificationLogService.getMailUnSentLogs();
       for (Map.Entry<String, List<NotificationLog>> entry : logs.entrySet()) {
           String Email = firebaseUserService.getEmailFromUid(entry.getKey());
          String message = "";
           for(NotificationLog log : entry.getValue()){
               if (!log.isMailSent()){
                message += log.getEvent().getTitle() + " : " + log.getEvent().getDescription() + "\n";
               log.setMailSent(true);
               notificationLogService.save(log);
               }
           }
           if (!message.equals("")) {


           LocalDate date = LocalDate.now();
           emailService.sendEmail(Email,"Notification du "+date  ,message);
           }
       }
      /*  for(NotificationLog log : logs){
            String Email = firebaseUserService.getEmailFromUid(log.getEvent().getUser().getUid());
            emailService.sendEmail(Email,"Notification","Nouvelle tache a effectuer");
        }*/

    }


}
