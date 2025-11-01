package com.eventsphere.eventspherebackend.user;

// Import the libraries we need from "Jakarta Persistence" (which is what JPA stands for)
// and Lombok (our code-writing helper).
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/*
 * These are Lombok annotations. They automatically write code for you in the background!
 * @Data: Creates all getters (user.getName()), setters (user.setName(...)), 
 * and other helpful methods (toString, equals, hashCode).
 * @Builder: Gives you a nice "builder" pattern to create new User objects.
 * @NoArgsConstructor: Creates an empty constructor (new User()). JPA needs this.
 * @AllArgsConstructor: Creates a constructor with all fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // This annotation marks this class as a database "entity" (a model).
// We *must* use @Table. The name "user" is a protected keyword in PostgreSQL.
// So, we tell Spring to name the actual table in the database "_user".
@Table(name = "_user") 
public class User {

    @Id // Marks this field as the Primary Key (the unique ID).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells Postgres to auto-increment this number (1, 2, 3...)
    private Long id;

    // @Column(nullable = false) means this field cannot be empty in the database.
    @Column(nullable = false) 
    private String name;

    // @Column(unique = true) means no two users can have the same email.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // A simple string for the role (e.g., "USER" or "ADMIN")
    private String role;
}

