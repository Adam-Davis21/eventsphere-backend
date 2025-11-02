package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.guest.Guest;
import com.eventsphere.eventspherebackend.guest.GuestRepository;
import com.eventsphere.eventspherebackend.task.Task;
import com.eventsphere.eventspherebackend.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000") // ✅ allow frontend requests
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final GuestRepository guestRepository;
    private final TaskRepository taskRepository;

    // ✅ Create Event
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (event.getDateTime() == null) {
            event.setDateTime(LocalDateTime.now());
        }

        // Ensure empty lists instead of null
        if (event.getGuests() == null) event.setGuests(new ArrayList<>());
        if (event.getTasks() == null) event.setTasks(new ArrayList<>());

        Event saved = eventRepository.save(event);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    // ✅ Get specific event with guests & tasks
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();

        // Force initialize collections
        event.getGuests().size();
        event.getTasks().size();

        return ResponseEntity.ok(event);
    }

    // ✅ Delete Event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    
    
}
