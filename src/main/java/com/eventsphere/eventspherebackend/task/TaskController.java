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

    // ✅ Add new task
    @PostMapping
    public ResponseEntity<Task> addTask(@PathVariable Long eventId, @RequestBody Task task) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        task.setEvent(event);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    // ✅ Get all tasks for an event
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long eventId) {
        return ResponseEntity.ok(taskRepository.findByEventId(eventId));
    }

    // ✅ Update task completion (toggle)
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTaskCompletion(
            @PathVariable Long eventId,
            @PathVariable Long taskId,
            @RequestBody Task updatedTask
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure the task actually belongs to this event
        if (!task.getEvent().getId().equals(eventId)) {
            return ResponseEntity.badRequest().build();
        }

        task.setCompleted(updatedTask.isCompleted());
        return ResponseEntity.ok(taskRepository.save(task));
    }

    // ✅ Delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long eventId, @PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure it belongs to this event
        if (!task.getEvent().getId().equals(eventId)) {
            return ResponseEntity.badRequest().build();
        }

        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }
}
