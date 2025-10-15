package aau.sw.controller;
import aau.sw.model.Case;
import aau.sw.model.User;
import aau.sw.repository.CaseRepository;
import aau.sw.service.UserService;
import aau.sw.repository.UserRepository;

main
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);

    }
  
    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody String name) {
        svc.updateUser(id, name);
        userRepository.findById(id).ifPresent(user -> {
            user.setName(name);
            userRepository.save(user);
        });
  }
}
