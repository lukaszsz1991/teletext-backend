package pl.studia.teletext.teletext_backend.domain.models.teletext;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TeletextCategory {
  NEWS(
      "Wiadomości", "Informacje z kraju i świata", 100, new TeletextSource[] {TeletextSource.NEWS}),
  SPORTS(
      "Sport",
      "Wyniki i aktualności sportowe",
      200,
      new TeletextSource[] {TeletextSource.SPORT_MATCHES, TeletextSource.SPORT_TABLE}),
  LOTTERY(
      "Gry losowe",
      "Wyniki lotto i innych gier",
      300,
      new TeletextSource[] {TeletextSource.LOTTERY}),
  TV("Program TV", "Program telewizyjny", 400, new TeletextSource[] {TeletextSource.TV_PROGRAM}),
  WEATHER(
      "Pogoda", "Prognozy i warunki pogodowe", 500, new TeletextSource[] {TeletextSource.WEATHER}),
  JOBS(
      "Oferty pracy",
      "Aktualne oferty zatrudnienia",
      600,
      new TeletextSource[] {TeletextSource.JOB_OFFERS}),
  HOROSCOPE(
      "Horoskop",
      "Dzienne i tygodniowe horoskopy",
      700,
      new TeletextSource[] {TeletextSource.HOROSCOPE}),
  FINANCE(
      "Finanse",
      "Kursy, giełda i ekonomia",
      800,
      new TeletextSource[] {TeletextSource.EXCHANGE_RATE}),
  MISC("Różne", "Pozostałe informacje", 900, new TeletextSource[] {TeletextSource.MANUAL});

  private final String title;
  private final String description;
  private final int mainPage;
  private final TeletextSource[] mappedSources;

  /**
   * Konstruktor enuma TeletextCategory. Waliduje strony główne kategorii. Strony muszą być między
   * 100 a 900 i wielokrotnościami 100.
   *
   * @param title tytuł kategorii
   * @param description krótki opis kategorii
   * @param mainPage strona główna kategorii. Musi być między 100 a 900 i wielokrotnością 100.
   *     Podstrony będą mogły przyjmować wartości od mainPage + 1 do mainPage + 99
   * @param mappedSources tablica źródeł danych powiązanych z kategorią. Musi pokrywać się z
   *     wartościami w {@link
   *     pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse}
   */
  TeletextCategory(String title, String description, int mainPage, TeletextSource[] mappedSources) {
    validateMainPage(mainPage);
    this.title = title;
    this.description = description;
    this.mainPage = mainPage;
    this.mappedSources = mappedSources;
  }

  @JsonCreator
  public static TeletextCategory fromString(String value) {
    return TeletextCategory.valueOf(value.trim().toUpperCase());
   }

  private static void validateMainPage(int mainPage) {
    if (mainPage < 100 || mainPage > 900)
      throw new IllegalArgumentException("Strona kategorii musi być między 100 a 900");
    if (mainPage % 100 != 0)
      throw new IllegalArgumentException("Strona kategorii musi być wielokrotnością 100");
  }
}
