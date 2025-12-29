package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto;

import java.time.LocalDateTime;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextCategoryResponse;

public record TeletextPageResponse(
    Long id,
    Integer pageNumber,
    String title,
    TeletextCategoryResponse category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
