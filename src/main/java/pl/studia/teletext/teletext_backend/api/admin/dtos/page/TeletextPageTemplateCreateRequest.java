package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

import java.util.Map;

public record TeletextPageTemplateCreateRequest(
    String name,
    TeletextSource source,
    TeletextCategory category,
    Map<String, Object> configJson) {}
