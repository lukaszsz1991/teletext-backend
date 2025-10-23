package pl.studia.teletext.teletext_backend.api.admin.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
  @NotNull(message = "Username is required")
  @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
  String username,
  @NotNull(message = "Email is required")
  @Email(message = "Email should be valid")
  @Size(min = 5, max = 255, message = "Email must be between 5 and 255 characters")
  String email
){}
