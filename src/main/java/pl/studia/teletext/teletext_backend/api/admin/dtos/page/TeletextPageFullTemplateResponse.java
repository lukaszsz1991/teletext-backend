package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;

public record TeletextPageFullTemplateResponse(
    Long id,
    String name,
    String source,
    String category,
    Map<String, Object> configJson,
    List<TeletextPageResponse> pages,
    String createdAt,
    String updatedAt) {}
