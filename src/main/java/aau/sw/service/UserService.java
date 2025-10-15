package aau.sw.service;
import aau.sw.model.User;
import aau.sw.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User createUser(User user){
        return repo.save(user);
    }
}
