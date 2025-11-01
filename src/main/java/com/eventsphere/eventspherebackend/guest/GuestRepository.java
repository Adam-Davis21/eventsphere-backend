// GuestRepository.java
package com.eventsphere.eventspherebackend.guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.eventsphere.eventspherebackend.event.Event;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByEventId(Long eventId);
    List<Guest> findByEvent(Event event);
}
