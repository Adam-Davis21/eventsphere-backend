package com.eventsphere.eventspherebackend.task;

import com.eventsphere.eventspherebackend.event.Event;
import com.eventsphere.eventspherebackend.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    public Task createTask(Long eventId, String title) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Task task = Task.builder()
                .title(title)
                .event(event)
                .build();
        return taskRepository.save(task);
    }

    public List<Task> getTasksByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return taskRepository.findByEvent(event);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
