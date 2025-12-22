package pl.studia.teletext.teletext_backend.config.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {
    var problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN, "Nie masz uprawnień, aby uzyskać dostęp do tego zasobu");
    problemDetail.setTitle("Odmowa dostępu");

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/problem+json");
    objectMapper.writeValue(response.getWriter(), problemDetail);
  }
}
