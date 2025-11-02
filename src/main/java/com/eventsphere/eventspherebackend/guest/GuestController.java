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

    // ✅ Add a new guest
    @PostMapping
    public ResponseEntity<Guest> addGuest(@PathVariable Long eventId, @RequestBody Guest guest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        guest.setEvent(event);
        guest.setRsvp("Pending"); // default RSVP status
        return ResponseEntity.ok(guestRepository.save(guest));
    }

    // ✅ Get all guests for a specific event
    @GetMapping
    public ResponseEntity<List<Guest>> getGuests(@PathVariable Long eventId) {
        return ResponseEntity.ok(guestRepository.findByEventId(eventId));
    }

    // ✅ Update RSVP status for a guest
    @PutMapping("/{guestId}")
    public ResponseEntity<Guest> updateRsvp(
            @PathVariable Long eventId,
            @PathVariable Long guestId,
            @RequestBody Guest updatedGuest
    ) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        // Ensure the guest actually belongs to this event
        if (!guest.getEvent().getId().equals(eventId)) {
            return ResponseEntity.badRequest().build();
        }

        guest.setRsvp(updatedGuest.getRsvp());
        return ResponseEntity.ok(guestRepository.save(guest));
    }

    // ✅ Delete a guest
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long eventId, @PathVariable Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        // Ensure the guest belongs to this event
        if (!guest.getEvent().getId().equals(eventId)) {
            return ResponseEntity.badRequest().build();
        }

        guestRepository.delete(guest);
        return ResponseEntity.noContent().build();
    }
}
