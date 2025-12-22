package pl.studia.teletext.teletext_backend.api.admin.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @NotNull(message = "Nazwa użytkownika jest wymagana")
        @Size(min = 5, max = 50, message = "Nazwa użytkownika musi mieć od {min} do {max} znaków")
        String username,
    @NotNull(message = "Email jest wymagany")
        @Email(message = "Nieprawidłowy format email")
        @Size(min = 5, max = 255, message = "Email musi mieć od {min} do {max} znaków")
        String email) {}
