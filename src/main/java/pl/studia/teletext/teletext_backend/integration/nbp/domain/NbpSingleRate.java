package pl.studia.teletext.teletext_backend.integration.nbp.domain;

import java.time.LocalDate;

public record NbpSingleRate(LocalDate effectiveDate, double bid, double ask) {}
