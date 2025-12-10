package pl.studia.teletext.teletext_backend.api.admin.dtos.page_template;

import java.util.Map;

public record TeletextPageTemplateResponse(
    Long id,
    String name,
    String source,
    String category,
    Map<String, Object> configJson,
    String createdAt,
    String updatedAt) {}
