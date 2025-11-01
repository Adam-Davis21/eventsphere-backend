package com.eventsphere.eventspherebackend.auth;

import lombok.Data;

@Data // <-- THIS WAS MISSING
public class LoginRequest {
    
    private String email;
    private String password;

}