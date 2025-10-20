package pl.studia.teletext.teletext_backend.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.dtos.SingleTeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.models.TeletextPage;
import pl.studia.teletext.teletext_backend.models.TeletextPageContent;
import pl.studia.teletext.teletext_backend.models.TeletextPageStats;

@Mapper(componentModel = "spring")
public interface TeletextPageMapper {

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "content", source = "content")
  TeletextPageResponse toPageResponse(TeletextPage page);

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
