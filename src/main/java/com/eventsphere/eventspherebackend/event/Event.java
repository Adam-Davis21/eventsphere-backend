package com.eventsphere.eventspherebackend.event;

import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.guest.Guest;
import com.eventsphere.eventspherebackend.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    @JsonBackReference(value = "user-events")
    private User host;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference(value = "event-guests")
    private List<Guest> guests = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference(value = "event-tasks")
    private List<Task> tasks = new ArrayList<>();

    // ✅ Added constructor so we can assign event by ID when adding guests
    public Event(Long id) {
        this.id = id;
    }
}
