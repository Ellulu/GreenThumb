
package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.NotificationLogRepository;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationLogService {



    private final NotificationLogRepository notificationLogRepository;
    private final EventService eventService;

    public NotificationLogService( NotificationLogRepository notificationLogRepository, EventService eventService) {


        this.notificationLogRepository = notificationLogRepository;
        this.eventService = eventService;
    }

    private void saverNotification(NotificationLog notificationLog) {

        notificationLogRepository.save(notificationLog);
    }

    public List<NotificationLog> getAllNotificationLogs() {
        return notificationLogRepository.findByIsSentFalse();
    }


    private NotificationLog notificationLogExists(Event event) {

        for (NotificationLog notificationLog : event.getNotificationLogs()){
            if(notificationLog.timeMatch()){
                return notificationLog;
            }
        }
        return null;


    }
    public void notifyUser(Event event) {
        NotificationLog notificationLog =  notificationLogExists(event);
        if (notificationLog != null) {
            System.out.println("Le log existe déjà");
            System.out.println("Vérifier si il a été lu ou pas");
        }else{
            System.out.println("Le log n'existe pas");
            System.out.println("Créer un nouveau log eet notifier l'utilisateur");

            saverNotification(new NotificationLog(LocalDateTime.now(), event));
        }

    }

    public void saveNotifForNewEvent(Event event){

            saverNotification(new NotificationLog(LocalDateTime.now(), event));

    }

    public void cleanOldLogs(int retentionDays) {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(retentionDays);
        notificationLogRepository.deleteByNotificationDateBefore(thresholdDate);

    }
    private void GetUpcomingEvents() {

        List<Event> recurringEvents = eventService.getAllEventForBack();

        for (Event event : recurringEvents) {
            notifyUser(event);


        }
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkForUpcomingEvents() {
        System.out.println("on entre dans la boucle");

        GetUpcomingEvents();

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanOldLogs() {
        int retentionDays = 30;
      cleanOldLogs(retentionDays);
        System.out.println("Old notification logs cleaned.");
    }

    public void save(NotificationLog log) {

        notificationLogRepository.save(log);
    }
}
