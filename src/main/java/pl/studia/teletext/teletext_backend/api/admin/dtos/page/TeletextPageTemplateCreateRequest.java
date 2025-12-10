package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

import java.util.Map;

public record TeletextPageTemplateCreateRequest(
    @NotBlank(message = "Nazwa nie może być pusta") String name,
    @NotNull(message = "Pole source nie może być puste") TeletextSource source,
    @NotNull(message = "Kategoria nie może być pusta") TeletextCategory category,
    Map<String, Object> configJson) {}
