package aau.sw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
  private String secret;
  private long ttlSeconds;
  private long refreshTtlSeconds;

  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }

  public long getTtlSeconds() { return ttlSeconds; }
  public void setTtlSeconds(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }

  public long getRefreshTtlSeconds() { return refreshTtlSeconds; }
  public void setRefreshTtlSeconds(long refreshTtlSeconds) { this.refreshTtlSeconds = refreshTtlSeconds; }
}
