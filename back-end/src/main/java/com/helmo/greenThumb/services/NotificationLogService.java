
package com.helmo.greenThumb.services;

import com.helmo.greenThumb.infrastructures.NotificationLogRepository;
import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        }else{


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

   // @Scheduled(fixedRate = 3600000)
   @Scheduled(cron = "0 1 0 * * ?")
    public void checkForUpcomingEvents() {

System.out.println("Checking for upcoming events");
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

    public List<NotificationLog> findAllNotificationByUser(String uid) {
        List<NotificationLog> notiflist= notificationLogRepository.findByEventUserUid(uid);

        return (notiflist.isEmpty())? new ArrayList<>() : notiflist;
    }

    public void delete(Long id) {
        notificationLogRepository.deleteById(id);
    }

    public  Map<String, List<NotificationLog>>  getMailUnSentLogs() {
        List<NotificationLog> logs = notificationLogRepository.findByIsMailSentFalse();
        Map<String, List<NotificationLog>> notification = new HashMap<>();
        for (NotificationLog log : logs) {
            String userId = log.getEvent().getUser().getUid();
            if(notification.containsKey(userId)){
                notification.get(userId).add(log);
            }else{
                List<NotificationLog> list = new ArrayList<>();
                list.add(log);
                notification.put(userId,list);
            }

        }
        return notification;

    }
}
