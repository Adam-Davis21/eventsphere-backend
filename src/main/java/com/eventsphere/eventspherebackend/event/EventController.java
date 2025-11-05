package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getUserEvents(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(eventRepository.findByHost(user));
    }

    // ✅ NEW: Fetch a single event by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id, Principal principal) {

        // ✅ Identify current logged-in user
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Fetch event
        Event event = eventRepository.findById(id).orElse(null);

        if (event == null) {
            return ResponseEntity.status(404).body("Event not found");
        }

        // ✅ Ensure the event belongs to this user
        if (!event.getHost().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.setHost(user);

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }
}
