package pl.studia.teletext.teletext_backend.api.admin.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.studia.teletext.teletext_backend.api.admin.dtos.PasswordChange;
import pl.studia.teletext.teletext_backend.domain.annotations.PasswordsMatch;

@PasswordsMatch
public record CreateUserRequest(
    @NotNull(message = "Nazwa użytkownika jest wymagana")
        @Size(min = 5, max = 50, message = "Nazwa użytkownika musi mieć od {min} do {max} znaków")
        String username,
    @NotNull(message = "Email jest wymagany")
        @Email(message = "Nieprawidłowy format email")
        @Size(min = 5, max = 255, message = "Email musi mieć od {min} do {max} znaków")
        String email,
    @NotNull(message = "Hasło jest wymagane")
        @Size(min = 5, max = 100, message = "Hasło musi mieć od {min} do {max} znaków")
        String password,
    @NotNull(message = "Hasło jest wymagane")
        @Size(min = 5, max = 100, message = "Hasło musi mieć od {min} do {max} znaków")
        String repeatPassword)
    implements PasswordChange {}
