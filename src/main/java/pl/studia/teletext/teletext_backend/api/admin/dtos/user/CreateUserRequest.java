package pl.studia.teletext.teletext_backend.api.admin.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.studia.teletext.teletext_backend.api.admin.dtos.PasswordChange;
import pl.studia.teletext.teletext_backend.domain.annotations.PasswordsMatch;

@PasswordsMatch
public record CreateUserRequest(
  @NotNull(message = "Username is required")
  @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
  String username,
  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
  String email,
  @NotNull(message = "Password is required")
  @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
  String password,
  @NotNull(message = "Password is required")
  @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
  String repeatPassword
) implements PasswordChange {}
