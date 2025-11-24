package pl.studia.teletext.teletext_backend.clients.highlightly.dtos;

public record HighlightlyStandingsInfo(
  HighlightlyLeague league,
  HighlightlyGroup[] groups
) {
  public record HighlightlyGroup(
    String name,
    HighlightlyStanding[] standings
  ) {
    public record HighlightlyStanding(
      HighlightlyTeam team,
      int position,
      int points,
      HighlightlyResults total
    ){
      public record HighlightlyResults(
        int wins,
        int draws,
        int loses,
        int games,
        int scoredGoals,
        int receivedGoals
      ){}
    }
  }
}
