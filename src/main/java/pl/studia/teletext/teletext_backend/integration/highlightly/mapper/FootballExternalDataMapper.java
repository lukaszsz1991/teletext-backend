package pl.studia.teletext.teletext_backend.integration.highlightly.mapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.common.dto.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.common.mapper.ExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballMatchesResponse;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballResponse;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballTableResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

@Component
public class FootballExternalDataMapper implements ExternalDataMapper<FootballResponse<?>> {

  @Override
  public ExternalDataResponse toExternalDataResponse(FootballResponse<?> source) {
    return switch (source) {
      case FootballTableResponse table -> toFootballTableExternalDataResponse(table);
      case FootballMatchesResponse matches -> toFootballMatchesExternalDataResponse(matches);
      default ->
          throw new IllegalArgumentException(
              "Unsupported football response type: " + source.getClass());
    };
  }

  @Override
  public Map<String, Object> toAdditionalData(FootballResponse<?> source) {
    return switch (source) {
      case FootballTableResponse table -> toFootballTableAdditionalData(table);
      case FootballMatchesResponse matches -> toFootballMatchesAdditionalData(matches);
      default ->
          throw new IllegalArgumentException(
              "Unsupported football response type: " + source.getClass());
    };
  }

  private ExternalDataResponse toFootballTableExternalDataResponse(FootballTableResponse source) {
    return new ExternalDataResponse(
        TeletextSource.SPORT_TABLE,
        source.getLeague() + " - sezon " + source.getSeason(),
        "Tabela ligowa - stan na dzień " + LocalDate.now(),
        toAdditionalData(source));
  }

  private Map<String, Object> toFootballTableAdditionalData(FootballTableResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("standings", source.getData().getStandings());
    return info;
  }

  private ExternalDataResponse toFootballMatchesExternalDataResponse(
      FootballMatchesResponse source) {
    return new ExternalDataResponse(
        TeletextSource.SPORT_MATCHES,
        source.getLeague() + " - sezon " + source.getSeason(),
        "Mecze w tygodniu " + source.getData().getWeek(),
        toAdditionalData(source));
  }

  private Map<String, Object> toFootballMatchesAdditionalData(FootballMatchesResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("matches", source.getData().getMatches());
    return info;
  }
}
