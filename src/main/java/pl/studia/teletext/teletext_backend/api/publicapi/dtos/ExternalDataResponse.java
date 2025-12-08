package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import java.util.Map;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

public record ExternalDataResponse(
    TeletextSource source, String title, String description, Map<String, Object> additionalData) {}
