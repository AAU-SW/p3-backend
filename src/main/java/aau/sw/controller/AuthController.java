package aau.sw.controller;

import aau.sw.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import aau.sw.aspect.LogExecution;
import aau.sw.dto.LoginReq;
import aau.sw.dto.RegisterReq;
import aau.sw.dto.UserDto;
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

  @PostMapping("/register")
  @LogExecution("Registered new user: ")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterReq req) {
    if (users.findByEmail(req.email()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "Email already in use"));
    }

    var u = new User();
    u.setEmail(req.email().trim().toLowerCase());
    u.setEncryptedPassword(encoder.encode(req.password()));
    u.setName(req.name().trim());
    u.setRole("admin");
    users.save(u);
    UserDto dto = new UserDto(u.getId(), u.getEmail(), u.getName(), u.getRole());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(dto);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginReq req) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
    String access = jwt.issue(req.email());
    String refresh = jwt.issueRefresh(req.email());
    return ResponseEntity.ok(Map.of("accessToken", access, "refreshToken", refresh));
  }

  @PostMapping("/refresh")
  public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
    String refreshToken = body.get("refreshToken");
    if (refreshToken == null || refreshToken.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of("error", "Refresh token required"));
    }

    try {
      String email = jwt.subject(refreshToken);
      String newAccess = jwt.issue(email);
      return ResponseEntity.ok(Map.of("accessToken", newAccess));
    } catch (ExpiredJwtException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Refresh token expired"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Invalid refresh token"));
    }
  }
}
