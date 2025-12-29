package pl.studia.teletext.teletext_backend.integration.news.domain;

import java.time.LocalDateTime;

public record NewsResponse(
    String link,
    String title,
    String description,
    String[] keywords,
    String[] creator,
    String[] country,
    String[] category,
    LocalDateTime pubDate,
    String sourceName,
    String sourceUrl) {}
