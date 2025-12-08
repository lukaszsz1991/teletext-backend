package pl.studia.teletext.teletext_backend.domain.models.teletext;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TeletextSource {
  EXCHANGE_RATE("exchange-rate"),
  HOROSCOPE("horoscope"),
  JOB_OFFERS("job-offers"),
  LOTTERY("lottery"),
  MANUAL("manual"),
  NEWS("news"),
  SPORT_MATCHES("sport-matches"),
  SPORT_TABLE("sport-table"),
  TV_PROGRAM("tv-program"),
  WEATHER("weather");

  private final String name;
}
