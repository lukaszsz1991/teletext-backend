package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

import java.time.LocalDateTime;

public record TeletextPageResponse (
    Long id,
    Integer pageNumber,
    String title,
    TeletextCategoryResponse category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
){}
