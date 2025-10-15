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

    public User createUser(User user){
        return repo.save(user);
      }

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

    public User getById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        var user = repo.findById(id);
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user.get();
    }
    public User getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        var user = repo.findByEmail(email);
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        return user.get();
    }

}

