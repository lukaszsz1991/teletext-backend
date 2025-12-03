package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

import java.time.LocalDateTime;

public record TeletextPageContentResponse(
    String title, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {}
