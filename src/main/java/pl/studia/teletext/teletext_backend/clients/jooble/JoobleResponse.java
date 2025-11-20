package pl.studia.teletext.teletext_backend.clients.jooble;

import java.time.LocalDateTime;
import java.util.List;

public record JoobleResponse(
  int totalCount,
  List<Job> jobs
) {
  public record Job(
    String title,
    String location,
    String snippet,
    String salary,
    String source,
    String type,
    String link,
    String company,
    LocalDateTime updated,
    long id
  ) {}
}
