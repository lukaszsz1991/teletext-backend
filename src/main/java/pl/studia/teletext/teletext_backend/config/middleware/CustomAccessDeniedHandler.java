package pl.studia.teletext.teletext_backend.config.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
    throws IOException {
    var problemDetail = ProblemDetail.forStatusAndDetail(
      HttpStatus.FORBIDDEN,
      "Forbidden: " + accessDeniedException.getMessage()
    );
    problemDetail.setTitle("Forbidden");
    problemDetail.setDetail("You do not have permission to access this resource");

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/problem+json");
    objectMapper.writeValue(response.getWriter(), problemDetail);
  }
}
