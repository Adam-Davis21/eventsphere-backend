package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.user.UserRepository;
import com.eventsphere.eventspherebackend.guest.GuestRepository;
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
    private final GuestRepository guestRepository;

    @GetMapping
    public ResponseEntity<List<Event>> getUserEvents(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(eventRepository.findByHost(user));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        event.setHost(user);
        return ResponseEntity.ok(eventRepository.save(event));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable Long eventId) {
        return eventRepository.findById(eventId)
                .map(event -> ResponseEntity.ok().body((Object) event))
                .orElseGet(() -> ResponseEntity.status(404).<Object>body("Event not found"));
    }

    @GetMapping("/{eventId}/guests")
    public ResponseEntity<?> getGuests(@PathVariable Long eventId) {
        return ResponseEntity.ok(guestRepository.findByEventId(eventId));
    }
}
