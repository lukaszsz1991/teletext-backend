package pl.studia.teletext.teletext_backend.clients.nbp;

public record NbpRateResponse(
  String currency,
  String code,
  NbpSingleRate[] rates
) { }
