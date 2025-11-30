package pl.studia.teletext.teletext_backend.domain.services.integrations;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.FootballMatchesResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.FootballTableResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.FootballMapper;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.clients.highlightly.HighlightlyClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class FootballService {

  private final HighlightlyClient highlightlyClient;
  private final FootballMapper footballMapper;

  public Mono<FootballTableResponse> getTableForLeague(FootballLeague league) {
    return highlightlyClient
        .getStandingsInfo(league, getCurrentSeasonYear())
        .map(footballMapper::toFootballTableResponse);
  }

  public Mono<FootballMatchesResponse> getMatchesForLeague(FootballLeague league, int week) {
    return highlightlyClient
        .getMatchesInfo(league, getCurrentSeasonYear())
        .filter(m -> m.date().get(WeekFields.of(DayOfWeek.MONDAY, 4).weekOfWeekBasedYear()) == week)
        .collectList()
        .map(result -> footballMapper.toFootballMatchesResponse(result, week))
        .onErrorResume(
            e -> {
              log.error(
                  "Error fetching football matches for league {}: {}", league, e.getMessage());
              var data = new FootballMatchesResponse.FootballMatchesData();
              data.setWeek(week);
              var emptyResponse = new FootballMatchesResponse();
              emptyResponse.setData(data);
              emptyResponse.setLeague(league.name());
              emptyResponse.setSeason(getCurrentSeasonYear());
              return Mono.just(emptyResponse);
            });
  }

  private int getCurrentSeasonYear() {
    var now = java.time.LocalDate.now();
    return now.getMonthValue() >= 8 ? now.getYear() : now.getYear() - 1;
  }
}
