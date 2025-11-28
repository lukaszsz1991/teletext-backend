package pl.studia.teletext.teletext_backend.domain.models.teletext;

import lombok.Getter;

@Getter
public enum TeletextCategory {
  NEWS("Wiadomości", "Informacje z kraju i świata", 101),
  SPORTS("Sport", "Wyniki i aktualności sportowe", 200),
  LOTTERY("Gry losowe", "Wyniki lotto i innych gier", 300),
  WEATHER("Pogoda", "Prognozy i warunki pogodowe", 500),
  JOBS("Oferty pracy", "Aktualne oferty zatrudnienia", 600),
  HOROSCOPE("Horoskop", "Dzienne i tygodniowe horoskopy", 700),
  FINANCE("Finanse", "Kursy, giełda i ekonomia", 800),
  MISC("Różne", "Pozostałe informacje", 900);

  private final String title;
  private final String description;
  private final int mainPage;

  /**
   * Konstruktor enuma TeletextCategory. Waliduje strony główne kategorii. Strony muszą być między 100 a 900 i wielokrotnościami 100.
   *
   * @param title       tytuł kategorii
   * @param description krótki opis kategorii
   * @param mainPage    strona główna kategorii. Musi być między 100 a 900 i wielokrotnością 100. Podstrony będą mogły przyjmować wartości od mainPage + 1 do mainPage + 99
   */
  TeletextCategory(String title, String description, int mainPage) {
    validateMainPage(mainPage);
    this.title = title;
    this.description = description;
    this.mainPage = mainPage;
  }

  private static void validateMainPage(int mainPage) {
    if (mainPage < 100 || mainPage > 900) throw new IllegalArgumentException("Strona kategorii musi być między 100 a 900");
    if (mainPage % 100 != 0) throw new IllegalArgumentException("Strona kategorii musi być wielokrotnością 100");
  }
}
