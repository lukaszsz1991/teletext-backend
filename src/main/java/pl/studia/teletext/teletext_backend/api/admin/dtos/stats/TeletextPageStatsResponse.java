package pl.studia.teletext.teletext_backend.api.admin.dtos.stats;

import java.util.List;

public record TeletextPageStatsResponse(
    Long views,
    List<SingleTeletextPageStatsResponse> stats
) { }
