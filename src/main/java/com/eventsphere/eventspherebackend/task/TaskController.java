package com.eventsphere.eventspherebackend.task;

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

    // ✅ Add Task (with priority + dueDate)
    @PostMapping
    public ResponseEntity<?> addTask(@PathVariable Long eventId, @RequestBody Task task) {
        return eventRepository.findById(eventId)
                .map(event -> {
                    task.setEvent(event);
                    var saved = taskRepository.save(task);
                    // force body type to Object so fallback types line up
                    return ResponseEntity.ok().body((Object) saved);
                })
                .orElseGet(() -> ResponseEntity.status(404).body((Object) "Event not found"));
    }

    // ✅ Get tasks for an event
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long eventId) {
        return ResponseEntity.ok(taskRepository.findByEventId(eventId));
    }

    // ✅ Update Task (priority, dueDate, completed)
    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long eventId,
            @PathVariable Long taskId,
            @RequestBody Task updated
    ) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    if (!task.getEvent().getId().equals(eventId)) {
                        return ResponseEntity.badRequest().body((Object) "Task does not belong to this event");
                    }

                    task.setTitle(updated.getTitle());
                    task.setPriority(updated.getPriority());
                    task.setDueDate(updated.getDueDate());
                    task.setCompleted(updated.isCompleted());

                    var saved = taskRepository.save(task);
                    return ResponseEntity.ok().body((Object) saved);
                })
                .orElseGet(() -> ResponseEntity.status(404).body((Object) "Task not found"));
    }

    // ✅ Delete Task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long eventId, @PathVariable Long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    if (!task.getEvent().getId().equals(eventId)) {
                        return ResponseEntity.badRequest().body((Object) "Task does not belong to this event");
                    }
                    taskRepository.delete(task);
                    return ResponseEntity.ok().body((Object) "Task deleted");
                })
                .orElseGet(() -> ResponseEntity.status(404).body((Object) "Task not found"));
    }
}
