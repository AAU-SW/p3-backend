package aau.sw.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint  {

    private static final org.slf4j.Logger log = 
    org.slf4j.LoggerFactory.getLogger(CustomAuthEntryPoint.class);



    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
    
    log.warn("Auth error: {}", authException.getMessage());

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    String json = """
            {
              "error": "Unauthorized",
              "message": "%s"
            }
            """.formatted(authException.getMessage());

    response.getWriter().write(json);
    }    
}
