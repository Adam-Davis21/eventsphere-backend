package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.event.dto.CreateEventRequest;
import com.eventsphere.eventspherebackend.event.dto.EventResponse;
import com.eventsphere.eventspherebackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse createEvent(CreateEventRequest request, User user) {
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .dateTime(request.getDateTime())
                .host(user)
                .build();

        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    public List<EventResponse> getEventsByUser(User user) {
        return eventRepository.findByHost(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<EventResponse> getEventById(Long id) {
        return eventRepository.findById(id).map(this::mapToResponse);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventResponse mapToResponse(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                // Convert LocalDateTime -> String to fix the type mismatch
                .dateTime(event.getDateTime() != null ? event.getDateTime().format(formatter) : null)
                .hostEmail(event.getHost().getEmail())
                .build();
    }
}
