package aau.sw.controller;

import aau.sw.security.JwtService;
import aau.sw.model.User;
import aau.sw.repository.UserRepository;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwt;

  public AuthController(UserRepository users, PasswordEncoder encoder,
      AuthenticationManager authManager, JwtService jwt) {
    this.users = users;
    this.encoder = encoder;
    this.authManager = authManager;
    this.jwt = jwt;
  }

  public record RegisterReq(String email, String password, String name) {
  }

  public record LoginReq(String email, String password) {
  }

  public record TokenRes(String accessToken) {
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterReq req) {
    if (users.findByEmail(req.email()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
    }
    var u = new User();
    u.setEmail(req.email());
    u.setEncryptedPassword(encoder.encode(req.password())); // uses your existing field
    u.setName(req.name());
    u.setRole("admin");
    users.save(u);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginReq req) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
    String access = jwt.issue(req.email());
    String refresh = jwt.issueRefresh(req.email());
    return ResponseEntity.ok(Map.of("accessToken", access, "refreshToken", refresh));
  }

  @PostMapping("/refresh")
  public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
    String refreshToken = body.get("refreshToken");
    String email = jwt.subject(refreshToken);
    String newAccess = jwt.issue(email);
    return ResponseEntity.ok(Map.of("accessToken", newAccess));
  }
}
