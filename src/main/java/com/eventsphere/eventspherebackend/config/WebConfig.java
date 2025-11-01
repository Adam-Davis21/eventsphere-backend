package com.eventsphere.eventspherebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // This configuration allows your React app (running on port 3000)
                // to make requests to your Spring Boot app (running on port 8080).
                registry.addMapping("/api/**") // Allow CORS for all API endpoints
                        .allowedOrigins("http://localhost:3000") // The origin of your React app
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies/auth tokens
            }
        };
    }
}