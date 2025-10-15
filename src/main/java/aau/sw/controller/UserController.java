package aau.sw.controller;

import aau.sw.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }
}
