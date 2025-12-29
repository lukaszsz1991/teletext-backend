package pl.studia.teletext.teletext_backend.common.dto;

import java.util.Map;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

public record ExternalDataResponse(
    TeletextSource source, String title, String description, Map<String, Object> additionalData) {}
