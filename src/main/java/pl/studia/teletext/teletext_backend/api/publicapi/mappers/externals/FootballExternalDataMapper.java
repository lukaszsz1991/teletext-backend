package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.FootballMatchesResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.FootballResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.FootballTableResponse;

@Component
public class FootballExternalDataMapper implements ExternalDataMapper<FootballResponse<?>> {

  @Override
  public ExternalDataResponse toExternalDataResponse(FootballResponse<?> source) {
    return switch(source) {
      case FootballTableResponse table -> toFootballTableExternalDataResponse(table);
      case FootballMatchesResponse matches -> toFootballMatchesExternalDataResponse(matches);
      default -> throw new IllegalArgumentException("Unsupported football response type: " + source.getClass());
    };
  }

  @Override
  public Map<String, Object> toAdditionalData(FootballResponse<?> source) {
    return switch(source) {
      case FootballTableResponse table -> toFootballTableAdditionalData(table);
      case FootballMatchesResponse matches -> toFootballMatchesAdditionalData(matches);
      default -> throw new IllegalArgumentException("Unsupported football response type: " + source.getClass());
    };
  }

  private ExternalDataResponse toFootballTableExternalDataResponse(FootballTableResponse source) {
    return new ExternalDataResponse(
      "sport",
      source.getLeague() + " - sezon " + source.getSeason(),
      "Tabela ligowa - stan na dzień " + LocalDate.now(),
      toAdditionalData(source)
    );
  }

  private Map<String, Object> toFootballTableAdditionalData(FootballTableResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("standings", source.getData().getStandings());
    return info;
  }

  private ExternalDataResponse toFootballMatchesExternalDataResponse(FootballMatchesResponse source) {
    return new ExternalDataResponse(
      "sport",
      source.getLeague() + " - sezon " + source.getSeason(),
      "Mecze w tygodniu " + source.getData().getWeek(),
      toAdditionalData(source)
    );
  }

  private Map<String, Object> toFootballMatchesAdditionalData(FootballMatchesResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("matches", source.getData().getMatches());
    return info;
  }
}
