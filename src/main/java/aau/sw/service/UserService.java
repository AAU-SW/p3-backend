package aau.sw.service;

import aau.sw.repository.UserRepository;
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
}

