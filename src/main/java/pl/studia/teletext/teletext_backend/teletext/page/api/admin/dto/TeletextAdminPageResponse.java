package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import java.time.LocalDateTime;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextFullPageContentResponse;

public record TeletextAdminPageResponse(
    Long id,
    String type,
    Integer pageNumber,
    TeletextCategoryResponse category,
    TeletextFullPageContentResponse content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt) {}
