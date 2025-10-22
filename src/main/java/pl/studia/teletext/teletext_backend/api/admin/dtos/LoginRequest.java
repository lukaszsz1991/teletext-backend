package pl.studia.teletext.teletext_backend.api.admin.dtos;

public record LoginRequest (
  String username,
  String password
){ }
