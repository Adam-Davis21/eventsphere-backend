package com.eventsphere.eventspherebackend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // ✅ Send invitation email to guest with RSVP link
    public void sendGuestInvitation(
            String to,
            String guestName,
            String eventTitle,
            String hostName,
            String eventDate,
            String eventLocation,
            Long eventId,
            Long guestId
    ) {
        String safeGuestName = (guestName != null && !guestName.isBlank()) ? guestName : "Guest";
        String safeEventTitle = (eventTitle != null && !eventTitle.isBlank()) ? eventTitle : "an event";
        String safeHostName = (hostName != null && !hostName.isBlank()) ? hostName : "the event host";
        String safeEventDate = (eventDate != null && !eventDate.isBlank()) ? eventDate : "Date to be announced";
        String safeEventLocation = (eventLocation != null && !eventLocation.isBlank()) ? eventLocation : "Location to be confirmed";

        // 📨 Generate RSVP link (for the *specific* guest)
        String rsvpLink = UriComponentsBuilder.fromHttpUrl("http://localhost:3000/rsvp")
                .queryParam("eventId", eventId)
                .queryParam("guestId", guestId)
                .toUriString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("🎉 Invitation: " + safeEventTitle);

        message.setText(String.format("""
                Hi %s,

                You’ve been invited to *%s* hosted by %s!

                📅 Date: %s
                📍 Location: %s

                Please RSVP using the link below:
                👉 %s

                Looking forward to seeing you there!

                — The EventSphere Team
                """,
                safeGuestName,
                safeEventTitle,
                safeHostName,
                safeEventDate,
                safeEventLocation,
                rsvpLink
        ));

        mailSender.send(message);
    }

    // ✅ Notify host when guest RSVPs (Accept / Decline)
    public void sendHostRsvpNotification(
            String hostEmail,
            String hostName,
            String guestName,
            String guestEmail,
            String eventTitle,
            String rsvpStatus,
            String eventDate,
            String eventLocation
    ) {
        if (hostEmail == null || hostEmail.isBlank()) return;

        String safeHostName = (hostName != null && !hostName.isBlank()) ? hostName : "Host";
        String safeGuestName = (guestName != null && !guestName.isBlank()) ? guestName : "Guest";
        String safeGuestEmail = (guestEmail != null && !guestEmail.isBlank()) ? guestEmail : "Unknown";
        String safeEventTitle = (eventTitle != null && !eventTitle.isBlank()) ? eventTitle : "your event";
        String safeStatus = (rsvpStatus != null && !rsvpStatus.isBlank()) ? rsvpStatus : "Updated";
        String safeEventDate = (eventDate != null && !eventDate.isBlank()) ? eventDate : "Date TBD";
        String safeEventLocation = (eventLocation != null && !eventLocation.isBlank()) ? eventLocation : "Location TBD";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(hostEmail);
        message.setSubject("📨 RSVP Update • " + safeEventTitle);

        message.setText(String.format("""
                Hi %s,

                An RSVP response has been recorded for your event *%s*:

                👤 Guest: %s (%s)
                ✅ Response: %s

                📅 Event Date: %s
                📍 Location: %s

                — EventSphere Notifications
                """,
                safeHostName,
                safeEventTitle,
                safeGuestName,
                safeGuestEmail,
                safeStatus,
                safeEventDate,
                safeEventLocation
        ));

        mailSender.send(message);
    }
}
