package aau.sw.controller;

import aau.sw.model.Case;
import aau.sw.model.User;
import aau.sw.repository.CaseRepository;
import aau.sw.repository.UserRepository;
import aau.sw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);

    }
}
