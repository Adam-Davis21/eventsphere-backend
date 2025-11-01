package com.eventsphere.eventspherebackend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Use Lombok's modern way to inject dependencies
public class AuthController {

    // This is injected by the @RequiredArgsConstructor
    private final AuthService authService;

    /**
     * This is the /register endpoint you've already built.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * This is the UPDATED /login endpoint.
     * It no longer returns a simple string.
     * It now calls the authService.login() method and
     * returns a ResponseEntity containing the LoginResponse (which holds the token).
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        // Call the service, get the response, and return it
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}