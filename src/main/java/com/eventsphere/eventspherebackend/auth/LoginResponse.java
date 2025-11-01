package com.eventsphere.eventspherebackend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // <-- THIS WAS MISSING
@Builder // <-- THIS WAS MISSING
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
}