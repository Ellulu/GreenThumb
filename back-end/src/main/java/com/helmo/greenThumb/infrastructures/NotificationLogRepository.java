package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationLogRepository  extends JpaRepository<NotificationLog, Long> {

    void deleteByNotificationDateBefore(LocalDateTime thresholdDate);

    List<NotificationLog> findByIsSentFalse();

    @Query("SELECT n FROM NotificationLog n WHERE n.event.user.uid = :uid")
    List<NotificationLog> findByEventUserUid(String uid);


    List<NotificationLog> findByIsMailSentFalse();
}
