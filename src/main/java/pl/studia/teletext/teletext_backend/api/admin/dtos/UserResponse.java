package pl.studia.teletext.teletext_backend.api.admin.dtos;

public record UserResponse(
  Long id,
  String username,
  String email,
  String role
) { }
