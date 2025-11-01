package com.eventsphere.eventspherebackend.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String dateTime;
    private String host;
    private String hostEmail;
}
