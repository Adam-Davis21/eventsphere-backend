// EventRepository.java
package com.eventsphere.eventspherebackend.event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.eventsphere.eventspherebackend.user.User;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByHost(User host);
}