package com.eventsphere.eventspherebackend.guest;

import com.eventsphere.eventspherebackend.email.EmailService;
import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Guest> addGuest(@PathVariable Long eventId, @RequestBody Guest guest) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        guest.setEvent(event);
        Guest savedGuest = guestRepository.save(guest);

        String eventDate = event.getDateTime() != null
                ? event.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy HH:mm"))
                : "Date TBD";

        String hostName = event.getHost() != null
                ? (event.getHost().getUsername() != null ? event.getHost().getUsername() : event.getHost().getEmail())
                : "Event Host";

        emailService.sendGuestInvitation(
                savedGuest.getEmail(),
                savedGuest.getName(),
                event.getTitle(),
                hostName,
                eventDate,
                event.getLocation(),
                eventId,
                savedGuest.getId()
        );

        return ResponseEntity.ok(savedGuest);
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getGuests(@PathVariable Long eventId) {
        return ResponseEntity.ok(guestRepository.findByEventId(eventId));
    }

    // ✅ Host updates RSVP from dashboard
    @PutMapping("/{guestId}")
    public ResponseEntity<Guest> updateGuestRSVP(@PathVariable Long eventId, @PathVariable Long guestId, @RequestBody Guest updatedGuest) {

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Event event = guest.getEvent();
        guest.setRsvp(updatedGuest.getRsvp());
        Guest saved = guestRepository.save(guest);

        // ✅ Notify Host
        if (event.getHost() != null && event.getHost().getEmail() != null) {
            String hostEmail = event.getHost().getEmail();
            String eventDate = event.getDateTime() != null
                    ? event.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy HH:mm"))
                    : "Date TBD";

            emailService.sendHostRsvpNotification(
                    hostEmail,
                    event.getHost().getUsername(),
                    guest.getName(),
                    guest.getEmail(),
                    event.getTitle(),
                    updatedGuest.getRsvp(),
                    eventDate,
                    event.getLocation()
            );
        }

        return ResponseEntity.ok(saved);
    }

    // ✅ Public RSVP endpoint (used from email link)
    @PutMapping("/rsvp/{guestId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> rsvpResponse(
            @PathVariable Long guestId,
            @RequestParam("response") String response
    ) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        Event event = guest.getEvent();
        guest.setRsvp(response);
        guestRepository.save(guest);

        // ✅ Notify Host
        if (event.getHost() != null && event.getHost().getEmail() != null) {
            String hostEmail = event.getHost().getEmail();
            String eventDate = event.getDateTime() != null
                    ? event.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy HH:mm"))
                    : "Date TBD";

            emailService.sendHostRsvpNotification(
                    hostEmail,
                    event.getHost().getUsername(),
                    guest.getName(),
                    guest.getEmail(),
                    event.getTitle(),
                    response,
                    eventDate,
                    event.getLocation()
            );
        }

        return ResponseEntity.ok("RSVP updated to: " + response);
    }

    // ✅ Delete Guest
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));
        guestRepository.delete(guest);
        return ResponseEntity.noContent().build();
    }
}
