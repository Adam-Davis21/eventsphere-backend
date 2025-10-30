package com.eventsphere.eventspherebackend.auth;

import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Tells Spring that this class is a "Service" and should be managed by it.
public class AuthService {

    // We need to talk to the database, so we "inject" the UserRepository.
    @Autowired
    private UserRepository userRepository;

    // We need to hash passwords, so we "inject" the PasswordEncoder we defined.
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This is the main registration logic.
     * It takes the DTO from the controller and creates a new user.
     */
    public void registerUser(SignUpRequest signUpRequest) {
        
        // 1. Create a new User object using the builder pattern from Lombok.
        User newUser = User.builder()
            .name(signUpRequest.getName())
            .email(signUpRequest.getEmail())
            // 2. IMPORTANT: Hash the plain-text password before saving it!
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            // 3. Set a default role for all new users.
            .role("USER")
            .build();

        // 4. Use the repository to save the new user to the database.
        userRepository.save(newUser);
    }

    // We will add a loginUser() method here later.
}

