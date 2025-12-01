package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

import java.time.LocalDateTime;
import java.util.Map;

public record TeletextFullPageContentResponse(
    String source,
    String title,
    String description,
    Map<String, Object> additionalData,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
