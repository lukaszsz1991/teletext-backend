package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

public record TeletextDetailedPageResponse(
    Long id,
    Integer pageNumber,
    TeletextCategoryResponse category,
    TeletextPageContentResponse content,
    String createdAt,
    String updatedAt
){}
