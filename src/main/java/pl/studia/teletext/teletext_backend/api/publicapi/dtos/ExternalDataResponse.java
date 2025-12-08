package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

import java.util.Map;

public record ExternalDataResponse(
  TeletextSource source, String title, String description, Map<String, Object> additionalData) {}
