package com.eventsphere.eventspherebackend.auth;

import lombok.Data;

// @Data is a Lombok annotation that creates getters, setters, 
// and other standard methods for us automatically.
@Data
public class SignUpRequest {
    
    // These fields perfectly match what we expect from a new user signing up
    private String name;
    private String email;
    private String password;
    
    // Note: We don't include 'id' or 'role' because the client
    // (the frontend) isn't supposed to provide those.
}