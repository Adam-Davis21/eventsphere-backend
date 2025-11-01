package com.eventsphere.eventspherebackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Note that this is an "interface", not a "class".
// We tell JpaRepository two things:
// 1. What entity is this repository for? (Answer: User)
// 2. What is the type of the Primary Key (the @Id)? (Answer: Long)
public interface UserRepository extends JpaRepository<User, Long> {
    
    // This is the magic of Spring Data JPA.
    // By simply declaring a method with this name, Spring will automatically
    // write the code to query the database and find a User by their email address.
    // We use "Optional" because the user might not exist.
    Optional<User> findByEmail(String email);
}

