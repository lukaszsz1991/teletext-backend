package pl.studia.teletext.teletext_backend.teletext.template.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

public record TeletextPageTemplateUpdateRequest(
    @NotBlank(message = "Nazwa nie może być pusta") String name,
    @NotNull(message = "Pole source nie może być puste") TeletextSource source,
    @NotNull(message = "Kategoria nie może być pusta") TeletextCategory category,
    Map<String, Object> configJson) {}
