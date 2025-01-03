package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationLogRepository  extends JpaRepository<NotificationLog, Long> {

    void deleteByNotificationDateBefore(LocalDateTime thresholdDate);

    List<NotificationLog> findByIsSentFalse();


}
