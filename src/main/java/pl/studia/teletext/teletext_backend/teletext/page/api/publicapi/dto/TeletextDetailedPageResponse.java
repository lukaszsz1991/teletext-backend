package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto;

import java.time.LocalDateTime;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextCategoryResponse;

public record TeletextDetailedPageResponse(
    Long id,
    String type,
    Integer pageNumber,
    TeletextCategoryResponse category,
    TeletextFullPageContentResponse content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
