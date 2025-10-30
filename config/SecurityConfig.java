package com.eventsphere.eventspherebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Tells Spring this is a configuration class
@EnableWebSecurity // This annotation turns on Spring Security
public class SecurityConfig {

    /**
     * Checklist item 2: Define a PasswordEncoder bean.
     * A "bean" is a Java object managed by Spring.
     * This bean defines the tool we will use to hash and verify passwords.
     * BCrypt is the industry-standard algorithm for this.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Checklist item 3: Configure security rules.
     * This "securityFilterChain" bean is the main firewall.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF: Not needed for a stateless REST API
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Define the security rules
            .authorizeHttpRequests(auth -> auth
                // Allow anyone (public access) to make requests to endpoints
                // that start with "/api/auth/" (e.g., /api/auth/register, /api/auth/login)
                .requestMatchers("/api/auth/**").permitAll() 
                
                // For any other request, the user MUST be authenticated (logged in).
                .anyRequest().authenticated() 
            )
            
            // 3. Make the API "stateless"
            // We won't use traditional sessions. This is standard for REST APIs
            // that will use tokens (like JWT) later.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}