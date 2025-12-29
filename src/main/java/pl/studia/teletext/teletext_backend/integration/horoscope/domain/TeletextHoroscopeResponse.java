package pl.studia.teletext.teletext_backend.integration.horoscope.domain;

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
