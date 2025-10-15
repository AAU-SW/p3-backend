package aau.sw.service;

import aau.sw.model.User;
import aau.sw.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> all() {
        return repo.findAll();
    }

    public User getById(String id) { return repo.findById(id).orElse(null); }
    public User getByEmail(String email) { return repo.findByEmail(email); }
}
