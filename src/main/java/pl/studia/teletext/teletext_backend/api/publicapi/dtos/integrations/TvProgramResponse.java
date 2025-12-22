package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TvProgramResponse(
  String channelName,
  LocalDate date,
  List<ProgramSlot> program
) {
  public record ProgramSlot(
    LocalTime time,
    String title,
    String description
  ) {}
}
