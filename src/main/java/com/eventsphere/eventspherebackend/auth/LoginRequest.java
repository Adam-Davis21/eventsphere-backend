package com.eventsphere.eventspherebackend.auth;

import lombok.Data;

@Data
public class LoginRequest {
    
    // For logging in, we only need the user's email and password.
    private String email;
    private String password;

}

