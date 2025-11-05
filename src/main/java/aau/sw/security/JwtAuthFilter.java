package aau.sw.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final CustomAuthEntryPoint customAuthEntryPoint;
  private final JwtService jwt;
  private final UserDetailsService uds;

  public JwtAuthFilter(JwtService jwt, UserDetailsService uds, CustomAuthEntryPoint customAuthEntryPoint) {
    this.jwt = jwt; this.uds = uds; this.customAuthEntryPoint = customAuthEntryPoint;
  };

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader(HttpHeaders.AUTHORIZATION);

    try {
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        String email = jwt.subject(token);
        UserDetails user = uds.loadUserByUsername(email);
        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      chain.doFilter(req, res);
    } catch (ExpiredJwtException ex) {
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException("Token expired", ex) {});
    } catch (Exception ex) {
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException("Invalid token", ex) {});
    }
  }

}