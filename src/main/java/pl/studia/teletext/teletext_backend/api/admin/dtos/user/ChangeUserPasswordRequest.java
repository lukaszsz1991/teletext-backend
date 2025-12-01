package pl.studia.teletext.teletext_backend.api.admin.dtos.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.studia.teletext.teletext_backend.api.admin.dtos.PasswordChange;
import pl.studia.teletext.teletext_backend.domain.annotations.PasswordsMatch;

@PasswordsMatch
public record ChangeUserPasswordRequest(
    @NotNull(message = "Password is required")
        @Size(min = 5, max = 100, message = "Password must be between 8 and 100 characters")
        String password,
    @NotNull(message = "Password is required")
        @Size(min = 5, max = 100, message = "Password must be between 8 and 100 characters")
        String repeatPassword)
    implements PasswordChange {}
