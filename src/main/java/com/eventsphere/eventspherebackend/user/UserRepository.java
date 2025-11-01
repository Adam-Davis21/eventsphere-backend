package com.eventsphere.eventspherebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; 

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // This is the new method you're adding.
    Optional<User> findByEmail(String email);

}

