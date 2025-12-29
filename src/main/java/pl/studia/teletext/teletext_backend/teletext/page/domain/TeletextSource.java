package pl.studia.teletext.teletext_backend.teletext.page.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
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

  @JsonCreator
  public static TeletextSource fromString(String value) {
    value = value.trim().toUpperCase().replaceAll("-", "_");
    try {
      return TeletextSource.valueOf(value);
    } catch (IllegalArgumentException ignored) {
      for (TeletextSource ttSource : TeletextSource.values()) {
        if (ttSource.getName().equals(value)) {
          return ttSource;
        }
      }
    }
    throw new IllegalArgumentException("Nie znaleziono źródła telegazety o nazwie: " + value);
  }
}
