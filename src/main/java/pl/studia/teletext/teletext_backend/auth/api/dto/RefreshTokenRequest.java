package pl.studia.teletext.teletext_backend.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "Aby odnowić token należy go przekazać") String refreshToken) {}
