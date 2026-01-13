package pl.studia.teletext.teletext_backend.teletext.template.api.dto;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageResponse;

public record TeletextPageFullTemplateResponse(
    Long id,
    String name,
    String source,
    String category,
    Map<String, Object> configJson,
    List<TeletextPageResponse> pages,
    String createdAt,
    String updatedAt) {}
