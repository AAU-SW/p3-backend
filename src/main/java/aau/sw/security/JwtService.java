package aau.sw.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import aau.sw.config.JwtProperties;

@Component
public class JwtService {
  private final JwtProperties props;

  private static final String TOKEN_TYPE_CLAIM = "token_type";
  private static final String ACCESS_TOKEN_TYPE = "access";
  private static final String REFRESH_TOKEN_TYPE = "refresh";

  public JwtService(JwtProperties props) {
    this.props = props;
  }

  public String issue(String subject) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(props.getTtlSeconds())))
        .signWith(Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  public String issueRefresh(String subject) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(props.getRefreshTtlSeconds())))
        .signWith(Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  public String subject(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean isRefreshToken(String token) {
    Claims claims = getClaims(token);
    String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
    return REFRESH_TOKEN_TYPE.equals(tokenType);
  }

  public boolean isAccessToken(String token) {
    Claims claims = getClaims(token);
    String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
    return ACCESS_TOKEN_TYPE.equals(tokenType);
  }

  public void validateRefreshToken(String token) {
    if (!isRefreshToken(token)) {
      throw new IllegalArgumentException("Token is not a refresh token");
    }
  }
}