package pl.studia.teletext.teletext_backend.api.admin.dtos.stats;

public record SingleTeletextPageStatsResponse(
    Long id,
    Integer pageNumber,
    String openedAt
) { }
