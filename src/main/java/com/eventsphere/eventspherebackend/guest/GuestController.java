package com.eventsphere.eventspherebackend.guest;

import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<Guest> addGuest(@PathVariable Long eventId, @RequestBody Guest guest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        guest.setEvent(event);
        return ResponseEntity.ok(guestRepository.save(guest));
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getGuests(@PathVariable Long eventId) {
        return ResponseEntity.ok(guestRepository.findByEventId(eventId));
    }
}
