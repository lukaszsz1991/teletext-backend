package pl.studia.teletext.teletext_backend.clients.nbp;

import java.time.LocalDate;

public record NbpSingleRate(
  LocalDate effectiveDate,
  double bid,
  double ask
) { }
