package com.eventsphere.eventspherebackend.auth;

import com.eventsphere.eventspherebackend.jwt.JwtService;
import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.user.UserRepository;

// Import the new tools we need
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // This Lombok annotation is a modern way to "inject" our dependencies
public class AuthService {

    // These are our "tools"
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * This is the registration logic you already built.
     * It's good to keep it here.
     */
    public void registerUser(SignUpRequest signUpRequest) {
        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword())) // Hash the password
                .role("USER") // Default role
                .build();
        userRepository.save(user);
    }

    /**
     * This is the new login logic.
     * It will check the password and return a LoginResponse (which contains the JWT).
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. This line tells Spring Security to check the email and password.
        // If the email doesn't exist or the password is wrong,
        // it will automatically throw an exception and stop here.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // 2. If the authentication was successful, we find the user in the database.
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after successful authentication. This should not happen."));
        
        // 3. We use our JwtService to generate a "key card" (token) for this specific user.
        var jwtToken = jwtService.generateToken(user);
        
        // 4. We create a LoginResponse object, put the token inside it, and return it.
        return LoginResponse.builder().token(jwtToken).build();
    }
}

