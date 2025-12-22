package pl.studia.teletext.teletext_backend.api.admin.dtos.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotNull(message = "Nazwa użytkownika jest wymagana")
        @Size(min = 5, max = 50, message = "Nazwa użytkownika musi mieć od {min} do {max} znaków")
        String username,
    @NotNull(message = "Hasło jest wymagane")
        @Size(min = 5, max = 100, message = "Hasło musi mieć od {min} do {max} znaków")
        String password) {}
