package pl.studia.teletext.teletext_backend.integration.jooble.domain;

import java.util.List;

public record JoobleResponse(int totalCount, List<Job> jobs) {
  public record Job(
      String title,
      String location,
      String snippet,
      String salary,
      String source,
      String type,
      String link,
      String company,
      String updated,
      long id) {}
}
