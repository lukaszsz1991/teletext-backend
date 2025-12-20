package pl.studia.teletext.teletext_backend.domain.services.pages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextCategoryMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;
import pl.studia.teletext.teletext_backend.exceptions.IllegalPageNumberException;

@Service
@RequiredArgsConstructor
public class TeletextCategoryService {

  private final TeletextCategoryMapper teletextCategoryMapper;
  private final TeletextPageService teletextPageService;

  /** Zwraca wszystkie kategorie teletekstu z enumu i mapuje je na odpowiedzi DTO. */
  public TeletextCategoryResponse[] getAllCategories() {
    return Arrays.stream(TeletextCategory.values())
        .map(
            cat ->
                teletextCategoryMapper.toCategoryResponse(
                    cat, getNextAvailablePageNumberInCategory(cat)))
        .toArray(TeletextCategoryResponse[]::new);
  }

  public TeletextCategoryResponse getCategoryByName(TeletextCategory category) {
    int nextFreePage = getNextAvailablePageNumberInCategory(category);
    return teletextCategoryMapper.toCategoryResponse(category, nextFreePage);
  }

  public TeletextCategory getCategoryByExternalDataSource(TeletextSource source) {
    return Arrays.stream(TeletextCategory.values())
        .filter(cat -> Arrays.asList(cat.getMappedSources()).contains(source))
        .findFirst()
        .orElse(TeletextCategory.MISC);
  }

  /**
   * Zwraca następny dostępny numer strony w danej kategorii teletekstu. Set wewnątrz pętli użyty w
   * celu poprawy wydajności.
   *
   * @param category Kategoria teletekstu
   * @return Następny dostępny numer strony
   * @throws IllegalPageNumberException jeśli nie ma dostępnych numerów stron w kategorii
   */
  public int getNextAvailablePageNumberInCategory(TeletextCategory category) {
    int mainPageNum = category.getMainPage();
    int start = mainPageNum + 1;
    int end = mainPageNum + 99;
    List<Integer> used =
        teletextPageService.getPagesByCategory(category, null).stream()
            .map(TeletextPageResponse::pageNumber)
            .sorted()
            .toList();
    Set<Integer> usedSet = new HashSet<>(used);
    for (int page = start; page <= end; page++) {
      if (!usedSet.contains(page)) {
        return page;
      }
    }
    throw new IllegalPageNumberException(
        "Brak dostępnych numerów stron dla kategorii " + category.getTitle());
  }
}
