package pl.studia.teletext.teletext_backend.api.admin.mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.SingleTeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageStats;

@Mapper(componentModel = "spring")
public interface TeletextPageStatsMapper {

  SingleTeletextPageStatsResponse toSinglePageStatsResponse(TeletextPageStats stat);

  default TeletextPageStatsResponse toPageStatsResponse(
      int pageNumber, List<TeletextPageStats> stats, boolean includeDetails) {
    long views = stats.size();
    List<SingleTeletextPageStatsResponse> details =
        includeDetails ? stats.stream().map(this::toSinglePageStatsResponse).toList() : List.of();
    return new TeletextPageStatsResponse(pageNumber, views, details);
  }

  default List<TeletextPageStatsResponse> toAllPageStatsResponse(
      List<TeletextPageStats> stats, boolean includeDetails) {
    return stats.stream()
        .collect(Collectors.groupingBy(t -> t.getPage().getPageNumber()))
        .entrySet()
        .stream()
        .map(
            entry -> {
              int pageNumber = entry.getKey();
              List<TeletextPageStats> pageStats = entry.getValue();
              return toPageStatsResponse(pageNumber, pageStats, includeDetails);
            })
        .toList();
  }
}
