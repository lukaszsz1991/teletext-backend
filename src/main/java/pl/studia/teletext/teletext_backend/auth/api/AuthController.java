package pl.studia.teletext.teletext_backend.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.teletext.teletext_backend.auth.api.dto.LoginRequest;
import pl.studia.teletext.teletext_backend.auth.api.dto.LoginResponse;
import pl.studia.teletext.teletext_backend.auth.service.AuthService;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(
    name = "Admin Authentication",
    description = "Endpoints for admin authentication and JWT token generation.")
public class AuthController {

  private final AuthService authService;

  @Operation(
      summary = "Authenticate user",
      description = "Validates credentials and returns a JWT token used for authorized access.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully authenticated — token info returned.")
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    var response = authService.authenticate(loginRequest);
    return ResponseEntity.ok(response);
  }
}
