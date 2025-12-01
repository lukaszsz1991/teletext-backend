package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

public record TeletextPageTemplateResponse(
    Long id,
    String name,
    String source,
    String category,
    String configJson,
    String createdAt,
    String updatedAt) {}
