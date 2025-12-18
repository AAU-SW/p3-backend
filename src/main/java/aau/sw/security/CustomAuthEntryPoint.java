package aau.sw.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint  {

    private static final org.slf4j.Logger log =
    org.slf4j.LoggerFactory.getLogger(CustomAuthEntryPoint.class);

    private static final List<String> ALLOWED_ORIGINS = List.of(
        "http://localhost:3000",
        "https://p3-frontend-tu5tr.ondigitalocean.app"
    );

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

    log.warn("Auth error: {}", authException.getMessage());

    // Add CORS headers to ensure the response reaches the frontend
    String origin = request.getHeader("Origin");
    if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
    }

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("UTF-8");

    String json = """
            {
              "error": "Unauthorized",
              "message": "%s"
            }
            """.formatted("Error logging in");

    response.getWriter().write(json);
    response.getWriter().flush();
    }
}
