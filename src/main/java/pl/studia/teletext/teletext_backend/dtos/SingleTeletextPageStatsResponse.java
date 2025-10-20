package pl.studia.teletext.teletext_backend.dtos;

public record SingleTeletextPageStatsResponse(
    Long id,
    Integer pageNumber,
    String openedAt
) { }
