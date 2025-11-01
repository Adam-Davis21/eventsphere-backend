package com.eventsphere.eventspherebackend.user;

// Imports for JPA (Database)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Imports for Lombok (to reduce boilerplate code)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Imports for Spring Security (UserDetails)
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Imports for Java Collections
import java.util.Collection;
import java.util.List;

/**
 * This is our User "Entity" class.
 * @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor are from Lombok.
 * They automatically create getters, setters, constructors, etc.
 * * @Entity tells Spring Data JPA that this class is a "blueprint" for a database table.
 * @Table(name = "_user") tells JPA to name the table "_user" because "user" is
 * often a reserved keyword in SQL.
 * * "implements UserDetails" is the "contract" from Spring Security.
 * It forces us to add the methods at the bottom.
 */
@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Table(name = "_user") 
public class User implements UserDetails { 

    @Id // Marks this as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    private String name;

    @Column(unique = true) // Makes sure no two users can have the same email
    private String email;

    private String password; // This will store the HASHED password

    private String role; // e.g., "USER" or "ADMIN"

    // -------------------------------------------------------------------
    // --- METHODS REQUIRED BY THE UserDetails INTERFACE (Spring Security) ---
    // -------------------------------------------------------------------

    /**
     * This method returns a list of roles (authorities) for the user.
     * Spring Security requires the "ROLE_" prefix.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    /**
     * This is the method Spring Security uses to get the "username".
     * In our application, we are using the email as the username.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * The rest of these methods are for account status.
     * For our simple app, we can just hard-code them to return "true".
     */

    @Override
    public boolean isAccountNonExpired() {
        return true; // Our accounts never expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Our accounts are never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Our passwords never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // All our accounts are enabled by default
    }
}

