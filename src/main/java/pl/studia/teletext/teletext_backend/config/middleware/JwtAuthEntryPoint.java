package pl.studia.teletext.teletext_backend.config.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException {
    var problemDetail = ProblemDetail.forStatusAndDetail(
      HttpStatus.UNAUTHORIZED,
      "Unauthorized: " + authException.getMessage()
    );
    problemDetail.setTitle("Unauthorized");
    problemDetail.setDetail("Invalid or missing JWT token");

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/problem+json");
    objectMapper.writeValue(response.getWriter(), problemDetail);
  }
}
