package pl.studia.teletext.teletext_backend.teletext.template.api.dto;

import java.util.Map;

public record TeletextPageTemplateResponse(
    Long id,
    String name,
    String source,
    String category,
    Map<String, Object> configJson,
    String createdAt,
    String updatedAt) {}
