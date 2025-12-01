package com.eventsphere.eventspherebackend.task;

import com.eventsphere.eventspherebackend.event.Event;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Priority priority = Priority.MEDIUM; // Default priority

    private LocalDate dueDate; // NEW FIELD ✅

    @Builder.Default
    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonBackReference(value = "event-tasks")
    private Event event;
}