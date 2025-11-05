package com.eventsphere.eventspherebackend.guest;

import com.eventsphere.eventspherebackend.email.EmailService;
import com.eventsphere.eventspherebackend.event.EventRepository;
import com.eventsphere.eventspherebackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events/{eventId}/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    // ✅ Add Guest + Send Invitation Email
    @PostMapping
    public ResponseEntity<?> addGuest(
            @PathVariable Long eventId,
            @RequestBody Guest newGuest
    ) {
        return eventRepository.findById(eventId)
                .<ResponseEntity<?>>map(event -> {
                    newGuest.setEvent(event);
                    newGuest.setRsvp("Pending");
                    guestRepository.save(newGuest);

                    // Send email, but don't fail the request if email fails
                    try {
                        User host = event.getHost();
                        emailService.sendGuestInvitation(
                                newGuest.getEmail(),
                                newGuest.getName(),
                                event.getTitle(),
                                host != null ? host.getUsername() : "Host",
                                event.getDateTime() != null ? event.getDateTime().toString() : "Date not set",
                                event.getLocation(),
                                event.getId(),
                                newGuest.getId()
                        );
                    } catch (Exception mailEx) {
                        // Optionally log mailEx
                    }

                    return ResponseEntity.ok(newGuest);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Event not found"));
    }

    // ✅ Update RSVP
    @PutMapping("/{guestId}")
    public ResponseEntity<?> updateRsvp(
            @PathVariable Long guestId,
            @RequestBody Guest updatedGuest
    ) {
        return guestRepository.findById(guestId)
                .<ResponseEntity<?>>map(existing -> {
                    existing.setRsvp(updatedGuest.getRsvp());
                    guestRepository.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Guest not found"));
    }

    // ✅ Delete Guest
    @DeleteMapping("/{guestId}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long guestId) {
        return guestRepository.findById(guestId)
                .<ResponseEntity<?>>map(existing -> {
                    guestRepository.delete(existing);
                    return ResponseEntity.ok("Guest deleted successfully");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Guest not found"));
    }

// ✅ Get a single guest for a specific event (used by RSVP page)
@GetMapping("/{guestId}")
public ResponseEntity<?> getGuest(
        @PathVariable Long eventId,
        @PathVariable Long guestId
) {
    return guestRepository.findById(guestId)
            .filter(g -> g.getEvent() != null && g.getEvent().getId().equals(eventId))
            .<ResponseEntity<?>>map(ResponseEntity::ok) // ✅ FIX: treat response generically
            .orElseGet(() -> ResponseEntity.status(404).body("Guest not found"));
}

}