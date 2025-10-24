package pl.studia.teletext.teletext_backend.api.admin.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.SingleTeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageStats;

@Mapper(componentModel = "spring")
public interface TeletextPageStatsMapper {

  SingleTeletextPageStatsResponse toSinglePageStatsResponse(TeletextPageStats stat);

  default TeletextPageStatsResponse toPageStatsResponse(List<TeletextPageStats> stats, boolean includeDetails) {
    long views = stats.size();
    List<SingleTeletextPageStatsResponse> details = includeDetails
      ? stats.stream().map(this::toSinglePageStatsResponse).toList()
      : List.of();
    return new TeletextPageStatsResponse(views, details);
  }
}
