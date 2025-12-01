package pl.studia.teletext.teletext_backend.api.admin.dtos.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotNull(message = "Username is required")
        @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
        String username,
    @NotNull(message = "Password is required")
        @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
        String password) {}
