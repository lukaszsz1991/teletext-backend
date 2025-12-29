package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto;

import java.time.LocalDateTime;

public record TeletextPageContentResponse(
    String title, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {}
