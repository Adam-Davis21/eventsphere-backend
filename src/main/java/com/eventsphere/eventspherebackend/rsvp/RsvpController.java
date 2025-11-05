package com.eventsphere.eventspherebackend.rsvp;

import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import com.eventsphere.eventspherebackend.guest.Guest;
import com.eventsphere.eventspherebackend.guest.GuestRepository;
import com.eventsphere.eventspherebackend.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rsvp")
@RequiredArgsConstructor
public class RsvpController {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    // ✅ Validate RSVP Link & Return Event + Guest Info
    @GetMapping
    public ResponseEntity<?> getRsvp(
            @RequestParam Long eventId,
            @RequestParam Long guestId
    ) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Guest guest = guestRepository.findById(guestId).orElse(null);

        if (event == null || guest == null || !guest.getEvent().getId().equals(eventId)) {
            return ResponseEntity.status(400).body("Invalid RSVP link.");
        }

        return ResponseEntity.ok(guest);
    }

    // ✅ Guest Submits RSVP Response
    @PostMapping
    public ResponseEntity<?> submitRsvp(
            @RequestParam Long eventId,
            @RequestParam Long guestId,
            @RequestParam String response // "Accepted" or "Declined"
    ) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Guest guest = guestRepository.findById(guestId).orElse(null);

        if (event == null || guest == null || !guest.getEvent().getId().equals(eventId)) {
            return ResponseEntity.status(400).body("Invalid RSVP link.");
        }

        // ✅ Update RSVP status
        guest.setRsvp(response);
        guestRepository.save(guest);

        // ✅ Send email to host about RSVP update
        emailService.sendHostRsvpNotification(
                event.getHost().getEmail(),
                event.getHost().getUsername(),
                guest.getName(),
                guest.getEmail(),
                event.getTitle(),
                response,
                event.getDateTime() != null ? event.getDateTime().toString() : "",
                event.getLocation()
        );

        return ResponseEntity.ok("RSVP recorded successfully.");
    }
}
