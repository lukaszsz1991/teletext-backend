package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

import java.time.LocalDate;

public record TeletextHoroscopeResponse(
    LocalDate day,
    String title,
    int ratingMood,
    int ratingLove,
    int ratingMoney,
    int ratingWork,
    int ratingLeasures,
    String prediction) {}
