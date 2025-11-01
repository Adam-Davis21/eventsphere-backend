package com.eventsphere.eventspherebackend.auth;

import com.eventsphere.eventspherebackend.jwt.JwtService;
import com.eventsphere.eventspherebackend.user.User;
import com.eventsphere.eventspherebackend.user.UserRepository;
import com.eventsphere.eventspherebackend.user.Role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void registerUser(SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getName()) // ✅ use username field
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication."));

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }
}
