package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

public record TeletextPageContentResponse (
    String title,
    String description,
    String createdAt,
    String updatedAt
) { }
