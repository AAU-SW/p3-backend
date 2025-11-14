package aau.sw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aau.sw.aspect.LogExecution;
import aau.sw.model.User;
import aau.sw.repository.UserRepository;
import aau.sw.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @PostMapping
    @LogExecution("Creating a new user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        var saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    @LogExecution("Fetching all users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @LogExecution("Deleting user by ID: ")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @LogExecution("Fetching user by ID: ")
    public ResponseEntity<User> getById(@PathVariable String id) {
        try {
            var user = svc.getById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/email/{email:.+}")
    @LogExecution("Fetching user by email: ")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        try {
            var user = svc.getByEmail(email);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @LogExecution("Updating user by ID: ")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody String name) {
        try {
            svc.updateUser(id, name);
            userRepository.findById(id).ifPresent(user -> {
                user.setName(name);
                userRepository.save(user);
            });
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();            
        }
    }

    @GetMapping("/me")
    @LogExecution("Fetching current authenticated user")
    public ResponseEntity<User> me(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(userRepository.findByEmail(email).orElseThrow());
    }

}
