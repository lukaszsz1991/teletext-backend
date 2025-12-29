package pl.studia.teletext.teletext_backend.integration.highlightly.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FootballMatchesResponse
    extends FootballResponse<FootballMatchesResponse.FootballMatchesData> {
  @Getter
  @Setter
  public static class FootballMatchesData {
    private int week;
    private FootballMatchDetails[] matches;
  }

  public record FootballMatchDetails(
      String round,
      LocalDateTime date,
      FootballMatchState state,
      String homeTeam,
      String awayTeam) {
    public record FootballMatchState(
        Integer clock, String currentScore, String penaltiesScore, String description) {}
  }
}
