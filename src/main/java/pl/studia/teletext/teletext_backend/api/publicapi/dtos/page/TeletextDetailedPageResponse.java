package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

import java.time.LocalDateTime;

public record TeletextDetailedPageResponse(
    Long id,
    Integer pageNumber,
    TeletextCategoryResponse category,
    TeletextFullPageContentResponse content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
