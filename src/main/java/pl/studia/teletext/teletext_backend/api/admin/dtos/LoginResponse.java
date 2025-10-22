package pl.studia.teletext.teletext_backend.api.admin.dtos;

public record LoginResponse(
  String token,
  String tokenType,
  Long expiresIn
) { }
