package pl.studia.teletext.teletext_backend.api.admin.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.admin.dtos.LoginRequest;
import pl.studia.teletext.teletext_backend.domain.services.security.AuthService;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(
    @Valid @RequestBody LoginRequest loginRequest
  ) {
    var response = authService.authenticate(loginRequest);
    return ResponseEntity.ok(response);
  }
}
