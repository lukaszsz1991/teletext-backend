package pl.studia.teletext.teletext_backend.domain.models.teletext;

import lombok.Getter;

@Getter
public enum TeletextCategory {
  NEWS("Wiadomości", "Informacje z kraju i świata", 100, new String[]{"news"}),
  SPORTS("Sport", "Wyniki i aktualności sportowe", 200, new String[]{"sport-matches", "sport-news"}),
  LOTTERY("Gry losowe", "Wyniki lotto i innych gier", 300, new String[]{"lottery"}),
  WEATHER("Pogoda", "Prognozy i warunki pogodowe", 500, new String[]{"weather"}),
  JOBS("Oferty pracy", "Aktualne oferty zatrudnienia", 600, new String[]{"job-offers"}),
  HOROSCOPE("Horoskop", "Dzienne i tygodniowe horoskopy", 700, new String[]{"hooroscope"}),
  FINANCE("Finanse", "Kursy, giełda i ekonomia", 800, new String[]{"exchange-rate"}),
  MISC("Różne", "Pozostałe informacje", 900, new String[]{"manual"});

  private final String title;
  private final String description;
  private final int mainPage;
  private final String[] mappedSources;

  /**
   * Konstruktor enuma TeletextCategory. Waliduje strony główne kategorii. Strony muszą być między 100 a 900 i wielokrotnościami 100.
   *
   * @param title         tytuł kategorii
   * @param description   krótki opis kategorii
   * @param mainPage      strona główna kategorii. Musi być między 100 a 900 i wielokrotnością 100. Podstrony będą mogły przyjmować wartości od mainPage + 1 do mainPage + 99
   * @param mappedSources tablica źródeł danych powiązanych z kategorią. Musi pokrywać się z wartościami w {@link pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse}
   */
  TeletextCategory(String title, String description, int mainPage, String[] mappedSources) {
    validateMainPage(mainPage);
    this.title = title;
    this.description = description;
    this.mainPage = mainPage;
    this.mappedSources = mappedSources;
  }

  private static void validateMainPage(int mainPage) {
    if (mainPage < 100 || mainPage > 900) throw new IllegalArgumentException("Strona kategorii musi być między 100 a 900");
    if (mainPage % 100 != 0) throw new IllegalArgumentException("Strona kategorii musi być wielokrotnością 100");
  }
}
