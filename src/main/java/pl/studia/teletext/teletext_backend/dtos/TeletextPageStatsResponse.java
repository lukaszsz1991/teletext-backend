package pl.studia.teletext.teletext_backend.dtos;

import java.util.List;

public record TeletextPageStatsResponse(
    Long views,
    List<SingleTeletextPageStatsResponse> stats
) { }
