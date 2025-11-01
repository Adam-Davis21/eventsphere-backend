package com.eventsphere.eventspherebackend.auth;

import lombok.Data;

@Data // <-- THIS WAS MISSING
public class SignUpRequest {
    
    private String name;
    private String email;
    private String password;
    
}