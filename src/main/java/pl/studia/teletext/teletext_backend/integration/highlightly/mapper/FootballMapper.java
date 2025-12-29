package pl.studia.teletext.teletext_backend.integration.highlightly.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballMatchesResponse;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballTableResponse;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.HighlightlyMatchesInfo;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.HighlightlyStandingsInfo;

@Mapper(componentModel = "spring")
public interface FootballMapper {

  // Table mappings
  @Mapping(target = "league", source = "league.name")
  @Mapping(target = "season", source = "league.season")
  @Mapping(target = "data.standings", source = "groups")
  FootballTableResponse toFootballTableResponse(HighlightlyStandingsInfo source);

  @Mapping(target = "team", source = "team.name")
  @Mapping(target = "wins", source = "total.wins")
  @Mapping(target = "draws", source = "total.draws")
  @Mapping(target = "loses", source = "total.loses")
  @Mapping(target = "scoredGoals", source = "total.scoredGoals")
  @Mapping(target = "receivedGoals", source = "total.receivedGoals")
  FootballTableResponse.FootballStandingDetails standingToDetails(
      HighlightlyStandingsInfo.HighlightlyGroup.HighlightlyStanding standing);

  FootballTableResponse.FootballStandingDetails[] mapStandingArray(
      HighlightlyStandingsInfo.HighlightlyGroup.HighlightlyStanding[] standings);

  default FootballTableResponse.FootballStandingDetails[] mapGroups(
      HighlightlyStandingsInfo.HighlightlyGroup[] groups) {
    if (groups == null || groups.length == 0)
      return new FootballTableResponse.FootballStandingDetails[0];
    return mapStandingArray(groups[0].standings());
  }

  // Matches mappings
  default FootballMatchesResponse toFootballMatchesResponse(
      List<HighlightlyMatchesInfo.HighlightlyMatchInfo> source, int week) {
    var response = new FootballMatchesResponse();
    if (source != null && !source.isEmpty()) {
      var league = source.getFirst().league();
      response.setLeague(league.name());
      response.setSeason(league.season());
    }
    var data = new FootballMatchesResponse.FootballMatchesData();
    data.setWeek(week);
    data.setMatches(matchListToDetailsArray(source));
    response.setData(data);
    return response;
  }

  default FootballMatchesResponse.FootballMatchDetails[] matchListToDetailsArray(
      List<HighlightlyMatchesInfo.HighlightlyMatchInfo> matches) {
    if (matches == null) return new FootballMatchesResponse.FootballMatchDetails[0];
    return matches.stream()
        .map(this::matchToDetails)
        .toArray(FootballMatchesResponse.FootballMatchDetails[]::new);
  }

  @Mapping(target = "homeTeam", source = "homeTeam.name")
  @Mapping(target = "awayTeam", source = "awayTeam.name")
  FootballMatchesResponse.FootballMatchDetails matchToDetails(
      HighlightlyMatchesInfo.HighlightlyMatchInfo match);

  @Mapping(target = "currentScore", source = "score.current")
  @Mapping(target = "penaltiesScore", source = "score.penalties")
  FootballMatchesResponse.FootballMatchDetails.FootballMatchState matchStateToState(
      HighlightlyMatchesInfo.HighlightlyMatchInfo.HighlightlyMatchState state);
}
