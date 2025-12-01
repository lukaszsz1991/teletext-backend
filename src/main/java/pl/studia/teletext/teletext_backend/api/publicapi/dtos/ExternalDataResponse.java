package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import java.util.Map;

public record ExternalDataResponse(
    String source, String title, String description, Map<String, Object> additionalData) {}
