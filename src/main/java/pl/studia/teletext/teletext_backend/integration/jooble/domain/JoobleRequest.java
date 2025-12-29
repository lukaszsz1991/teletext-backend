package pl.studia.teletext.teletext_backend.integration.jooble.domain;

import jakarta.validation.constraints.NotBlank;

public record JoobleRequest(
    @NotBlank(message = "Keywords must not be blank") String keywords,
    @NotBlank(message = "Location must not be blank") String location) {}
