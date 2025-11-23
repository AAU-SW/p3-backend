package aau.sw.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

  private final CustomAuthEntryPoint customAuthEntryPoint;
  private final JwtService jwt;
  private final UserDetailsService uds;

  @Value("${spring.profiles.active:prod}")
  private String activeProfile;

  public JwtAuthFilter(JwtService jwt, UserDetailsService uds, CustomAuthEntryPoint customAuthEntryPoint) {
    this.jwt = jwt;
    this.uds = uds;
    this.customAuthEntryPoint = customAuthEntryPoint;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader(HttpHeaders.AUTHORIZATION);

    try {
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);

        // Validate it's an access token (not a refresh token)
        if (!jwt.isAccessToken(token)) {
          log.warn("Attempted to use refresh token as access token - URI: {}", req.getRequestURI());
          SecurityContextHolder.clearContext();
          customAuthEntryPoint.commence(req, res,
              new AuthenticationException("Invalid token type, use access token") {
              });
          return;
        }

        String email = jwt.subject(token);
        UserDetails user = uds.loadUserByUsername(email);
        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      chain.doFilter(req, res);

    } catch (ExpiredJwtException ex) {
      String message = "Token has expired";
      if ("dev".equals(activeProfile)) {
        message += ": " + ex.getMessage();
      }
      log.warn("JWT expired - URI: {}, Details: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (MalformedJwtException ex) {
      String message = "Token is malformed";
      if ("dev".equals(activeProfile)) {
        message += ": " + ex.getMessage();
      }
      log.warn("Malformed JWT - URI: {}, Details: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (SignatureException ex) {
      String message = "Token signature is invalid";
      if ("dev".equals(activeProfile)) {
        message += ": " + ex.getMessage();
      }
      log.warn("JWT signature validation failed - URI: {}, Details: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (UnsupportedJwtException ex) {
      String message = "Token format is not supported";
      if ("dev".equals(activeProfile)) {
        message += ": " + ex.getMessage();
      }
      log.warn("Unsupported JWT - URI: {}, Details: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (IllegalArgumentException ex) {
      String message = "Token claims are invalid";
      if ("dev".equals(activeProfile)) {
        message += ": " + ex.getMessage();
      }
      log.warn("Invalid JWT claims - URI: {}, Details: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (UsernameNotFoundException ex) {
      String message = "User not found";
      log.warn("User not found for JWT - URI: {}, User: {}", req.getRequestURI(), ex.getMessage());
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;

    } catch (Exception ex) {
      String message = "Authentication failed";
      log.error("Unexpected error during JWT authentication - URI: {}, Error: {}",
          req.getRequestURI(), ex.getMessage(), ex);
      SecurityContextHolder.clearContext();
      customAuthEntryPoint.commence(req, res, new AuthenticationException(message, ex) {
      });
      return;
    }
  }
}