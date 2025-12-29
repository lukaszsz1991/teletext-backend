package pl.studia.teletext.teletext_backend.integration.tvp.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TvProgramResponse(String channelName, LocalDate date, List<ProgramSlot> program) {
  public record ProgramSlot(LocalDateTime time, String title, String description) {}
}
