package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FootballTableResponse
    extends FootballResponse<FootballTableResponse.FootballTableData> {

  @Getter
  @Setter
  public static class FootballTableData {
    private FootballStandingDetails[] standings;
  }

  public record FootballStandingDetails(
      String team,
      int position,
      int points,
      int wins,
      int draws,
      int loses,
      int scoredGoals,
      int receivedGoals) {}
}
