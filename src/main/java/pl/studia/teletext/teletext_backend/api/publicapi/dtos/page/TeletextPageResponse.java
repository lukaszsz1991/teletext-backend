package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

public record TeletextPageResponse (
    Long id,
    Integer pageNumber,
    String title,
    TeletextCategoryResponse category,
    String createdAt,
    String updatedAt
){}
