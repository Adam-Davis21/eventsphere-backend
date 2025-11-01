package com.eventsphere.eventspherebackend.task;

import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<Task> addTask(@PathVariable Long eventId, @RequestBody Task task) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        task.setEvent(event);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long eventId) {
        return ResponseEntity.ok(taskRepository.findByEventId(eventId));
    }
}
