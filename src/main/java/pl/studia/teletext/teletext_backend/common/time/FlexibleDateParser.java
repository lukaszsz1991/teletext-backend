package pl.studia.teletext.teletext_backend.common.time;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.List;

public class FlexibleDateParser {

  private static final DateTimeFormatter TVP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final List<DateTimeFormatter> FORMATTERS =
      List.of(
          DateTimeFormatter.ISO_OFFSET_DATE_TIME,
          DateTimeFormatter.ISO_LOCAL_DATE_TIME,
          DateTimeFormatter.ISO_LOCAL_DATE,
          TVP_FORMATTER);

  /**
   * Parse a date/time string into a {@link java.time.LocalDateTime}.
   *
   * <p>Supported input formats (tried in order):
   *
   * <ul>
   *   <li>ISO offset date-time (e.g. 2025-01-01T12:00:00+01:00)
   *   <li>ISO local date-time (e.g. 2025-01-01T12:00:00)
   *   <li>ISO local date (e.g. 2025-01-01) — returns start of day
   *   <li>Custom TVP format (e.g. 2025-01-01 12:00:00)
   * </ul>
   *
   * @param date the date/time string to parse
   * @return the parsed {@link java.time.LocalDateTime}
   * @throws java.time.format.DateTimeParseException if the input cannot be parsed
   */
  public static LocalDateTime parse(String date) {
    for (DateTimeFormatter f : FORMATTERS) {
      try {
        TemporalAccessor ta = f.parse(date);
        if (ta.query(TemporalQueries.offset()) != null)
          return OffsetDateTime.from(ta).toLocalDateTime();

        if (ta.query(TemporalQueries.localDate()) != null) {
          LocalDateTime ldt;
          try {
            ldt = LocalDateTime.from(ta);
          } catch (DateTimeException e) {
            ldt = LocalDate.from(ta).atStartOfDay();
          }
          return ldt;
        }
      } catch (DateTimeParseException ignored) {
      }
    }
    throw new DateTimeParseException("Nie udało się przekonwertować: " + date, date, -1);
  }

  public static String format(LocalDateTime value) {
    return TVP_FORMATTER.format(value);
  }
}
