package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextFullPageContentResponse;

import java.time.LocalDateTime;

public record TeletextAdminPageResponse(
  Long id,
  String type,
  Integer pageNumber,
  TeletextCategoryResponse category,
  TeletextFullPageContentResponse content,
  LocalDateTime createdAt,
  LocalDateTime updatedAt,
  LocalDateTime deletedAt
){}
