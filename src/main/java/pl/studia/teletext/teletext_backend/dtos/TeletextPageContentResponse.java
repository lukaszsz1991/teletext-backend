package pl.studia.teletext.teletext_backend.dtos;

public record TeletextPageContentResponse (
    Long id,
    String content,
    String createdAt,
    String updatedAt
) { }
