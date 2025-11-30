package pl.studia.teletext.teletext_backend.api.admin.dtos.stats;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public record TeletextPageStatsResponse(
    int pageNumber,
    long views,
    @JsonInclude(NON_EMPTY) List<SingleTeletextPageStatsResponse> details) {}
