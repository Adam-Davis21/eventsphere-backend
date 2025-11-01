package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByHost(User host);
}
