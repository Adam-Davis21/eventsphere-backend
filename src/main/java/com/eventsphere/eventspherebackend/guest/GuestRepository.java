package com.eventsphere.eventspherebackend.guest;

import com.eventsphere.eventspherebackend.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByEventId(Long eventId);

    List<Guest> findByEvent(Event event);

    // ⭐ NEW
    List<Guest> findByEmail(String email);

    // ⭐ NEW
    List<Guest> findByEmailAndRsvp(String email, String rsvp);
}
