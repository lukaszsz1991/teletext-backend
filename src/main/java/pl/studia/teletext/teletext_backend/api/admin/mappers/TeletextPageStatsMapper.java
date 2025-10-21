package pl.studia.teletext.teletext_backend.api.admin.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.admin.dtos.SingleTeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPageContent;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPageStats;

@Mapper(componentModel = "spring")
public interface TeletextPageStatsMapper {

  @Mapping(target = "pageNumber", source = "page.pageNumber")
  SingleTeletextPageStatsResponse toSinglePageStatsResponse(TeletextPageStats stat);

  default TeletextPageStatsResponse toPageStatsResponse(List<TeletextPageStats> stats) {
    var statsResponses = stats.stream()
        .map(this::toSinglePageStatsResponse)
        .toList();
    var views = (long) statsResponses.size();
    return new TeletextPageStatsResponse(views, statsResponses);
  }
}
