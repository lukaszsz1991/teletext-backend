package pl.studia.teletext.teletext_backend.integration.highlightly.domain;

import java.time.LocalDateTime;

public record HighlightlyMatchesInfo(
    HighlightlyPagination pagination, HighlightlyMatchInfo[] data) {
  public record HighlightlyPagination(int totalCount, int offset, int limit) {}

  public record HighlightlyMatchInfo(
      String round,
      LocalDateTime date,
      HighlightlyMatchState state,
      HighlightlyTeam homeTeam,
      HighlightlyTeam awayTeam,
      HighlightlyLeague league) {
    public record HighlightlyMatchState(
        Integer clock, HighlightlyMatchScore score, String description) {
      public record HighlightlyMatchScore(String current, String penalties) {}
    }
  }
}
