package pl.studia.teletext.teletext_backend.auth.api.dto;

public record LoginResponse(String token, String tokenType, Long expiresIn) {}
