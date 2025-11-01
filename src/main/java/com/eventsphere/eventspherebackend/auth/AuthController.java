package com.eventsphere.eventspherebackend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // This marks the class as a web controller
@RequestMapping("/api/auth") // All endpoints in this class will start with /api/auth
public class AuthController {

    // We "inject" the AuthService so we can call its methods.
    @Autowired
    private AuthService authService;

    /**
     * This is the /register endpoint.
     * @PostMapping("/register") maps HTTP POST requests to this method.
     * @RequestBody tells Spring to take the JSON from the request and
     * convert it into a SignUpRequest object.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {
        // We call our service to do the actual registration logic.
        authService.registerUser(signUpRequest);

        // We return a 200 OK response with a simple message.
        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * This is the /login endpoint.
     * We will implement the full logic for this in the next steps (JWT).
     * For now, it's a placeholder.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        // TODO: Add login logic here
        return ResponseEntity.ok("Login endpoint is under construction.");
    }
}

