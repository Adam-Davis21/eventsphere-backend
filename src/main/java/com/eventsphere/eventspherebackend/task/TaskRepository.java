// TaskRepository.java
package com.eventsphere.eventspherebackend.task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.eventsphere.eventspherebackend.event.Event;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEventId(Long eventId);
    List<Task> findByEvent(Event event);
}
