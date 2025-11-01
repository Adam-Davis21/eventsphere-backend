package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.event.dto.CreateEventRequest;
import com.eventsphere.eventspherebackend.event.dto.EventResponse;
import com.eventsphere.eventspherebackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request,
                                                     @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(eventService.createEvent(request, user));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getUserEvents(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(eventService.getEventsByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
