package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

public record TeletextPageContentResponse (
    String title,
    String description,
    String createdAt,
    String updatedAt
) { }
