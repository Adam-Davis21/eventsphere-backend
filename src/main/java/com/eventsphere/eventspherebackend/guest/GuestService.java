package com.eventsphere.eventspherebackend.guest;

import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final EventRepository eventRepository;

    public Guest addGuest(Long eventId, String name, String email) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Guest guest = Guest.builder()
                .name(name)
                .email(email)
                .event(event)
                .build();
        return guestRepository.save(guest);
    }

    public List<Guest> getGuestsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return guestRepository.findByEvent(event);
    }

    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
}
