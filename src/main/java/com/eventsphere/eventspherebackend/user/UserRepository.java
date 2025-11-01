package com.eventsphere.eventspherebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // <-- Make sure this import is added

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // This is the new method you're adding.
    // Spring Data JPA will automatically create the code to find a user by their email.
    Optional<User> findByEmail(String email);

}