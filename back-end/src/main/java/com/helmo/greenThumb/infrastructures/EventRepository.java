package com.helmo.greenThumb.infrastructures;

import com.helmo.greenThumb.model.Event;
import com.helmo.greenThumb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUser(User findUser);

}

