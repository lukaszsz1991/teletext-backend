package pl.studia.teletext.teletext_backend.api.admin.dtos.security;

public record LoginResponse(String token, String tokenType, Long expiresIn) {}
