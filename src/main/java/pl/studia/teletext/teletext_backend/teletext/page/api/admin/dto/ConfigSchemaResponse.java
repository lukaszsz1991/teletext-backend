package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import java.util.List;
import java.util.Map;

public record ConfigSchemaResponse(
    String source, List<String> required, List<String> optional, Map<String, String> types) {}
