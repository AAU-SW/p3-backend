package aau.sw.service;

import aau.sw.model.Auditable;
import aau.sw.repository.UserRepository;

import aau.sw.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuditableService {

    private final UserRepository userRepository;
    
    //injected UserRepository to make sure we can make CRUD to the database
    public AuditableService(UserRepository userRepository) {
    this.userRepository = userRepository;
    }
    
    //SubClass of a super class <T> 
    public <T extends Auditable> T setCreatedBy(T entity) {
        // Get authentication information from Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Check if a user is logged in and authentication is valid
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            // find the user whit email
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found: " + email));
            entity.setCreatedBy(user);
        }
        return entity;
        
    }
}
