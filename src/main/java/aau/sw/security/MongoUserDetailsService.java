package aau.sw.security;

import aau.sw.model.User;
import aau.sw.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements UserDetailsService {
  private final UserRepository users;
  public MongoUserDetailsService(UserRepository users) { this.users = users; }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User u = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    String role = (u.getRole() == null ? "user" : u.getRole());
    return org.springframework.security.core.userdetails.User
      .withUsername(u.getEmail())
      .password(u.getEncryptedPassword())
      .roles(role)
      .build();
  }
}
