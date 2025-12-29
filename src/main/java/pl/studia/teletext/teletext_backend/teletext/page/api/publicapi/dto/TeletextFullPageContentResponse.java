package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record TeletextFullPageContentResponse(
    String source,
    String title,
    String description,
    Map<String, Object> additionalData,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
