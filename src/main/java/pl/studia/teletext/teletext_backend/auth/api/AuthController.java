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
import pl.studia.teletext.teletext_backend.auth.api.dto.RefreshTokenRequest;
import pl.studia.teletext.teletext_backend.auth.service.AuthService;
import pl.studia.teletext.teletext_backend.auth.service.RefreshTokenService;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(
    name = "Admin Authentication",
    description = "Endpoints for admin authentication and tokens generation.")
public class AuthController {

  private final AuthService authService;
  private final RefreshTokenService refreshTokenService;

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

  @Operation(
      summary = "Refresh authorization",
      description =
          "Refresh authorization by passing valid refresh token in body. Returns new JWT and refresh tokens.")
  @ApiResponse(
      responseCode = "200",
      description = "Successfully refreshed authorization — token info returned.")
  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      @RequestBody @Valid RefreshTokenRequest request) {
    var response = authService.reauthenticate(request.refreshToken());
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Logout user",
      description =
          "Allows user to logout by deleting his refresh token. Frontend should remove JWT token.")
  @ApiResponse(
      responseCode = "204",
      description = "Successfully logged out — refresh token removed from database.")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
    refreshTokenService.removeToken(request.refreshToken());
    return ResponseEntity.noContent().build();
  }
}
