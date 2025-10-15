package aau.sw.service;

import aau.sw.model.User;
import aau.sw.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }


    @Transactional
    public void updateUser(String id, String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        var user = repo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found" + id));
            user.setName(newName);
            repo.save(user);
        };

    public List<User> all() {
        return repo.findAll();
    }

    public User getById(String id) { return repo.findById(id).orElse(null); }
    public User getByEmail(String email) { return repo.findByEmail(email); }

}

