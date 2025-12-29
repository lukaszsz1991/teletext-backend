package pl.studia.teletext.teletext_backend.integration.nbp.domain;

public record NbpRateResponse(String currency, String code, NbpSingleRate[] rates) {}
