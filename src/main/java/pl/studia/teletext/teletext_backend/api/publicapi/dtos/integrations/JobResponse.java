package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

import java.time.LocalDateTime;

public record JobResponse(
    int totalResults,
    String searchKeywords,
    String searchLocation,
    String location,
    String title,
    String snippet,
    String salary,
    String link,
    String company,
    LocalDateTime updatedAt) {}
