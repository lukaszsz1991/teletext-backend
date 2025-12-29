package pl.studia.teletext.teletext_backend.integration.horoscope.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record HoroscopeResponse(
    @JsonProperty("horoscope_day") LocalDate day, List<SignInfo> signs) {
  public record SignInfo(
      String title,
      @JsonProperty("rating_mood") int ratingMood,
      @JsonProperty("rating_love") int ratingLove,
      @JsonProperty("rating_money") int ratingMoney,
      @JsonProperty("rating_work") int ratingWork,
      @JsonProperty("rating_leasures") int ratingLeasures,
      String prediction) {}
}
