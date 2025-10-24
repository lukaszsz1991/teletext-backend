package pl.studia.teletext.teletext_backend.domain.services;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextPageStatsMapper;
import pl.studia.teletext.teletext_backend.exceptions.PageNotFoundException;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageStats;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageStatsRepository;

@Service
@RequiredArgsConstructor
public class TeletextPageStatsService {

  private final TeletextPageRepository pageRepository;
  private final TeletextPageStatsRepository statsRepository;
  private final TeletextPageStatsMapper mapper;

  @Transactional
  public void recordPageVisit(Long pageId) {
    var page = pageRepository.findById(pageId)
      .orElseThrow(() -> new PageNotFoundException("Page with id " + pageId + " not found"));
    var stat = new TeletextPageStats(page);
    statsRepository.save(stat);
  }

  public TeletextPageStatsResponse getStatsForPage(Integer pageNumber, Boolean includeDetails) {
    var stats = statsRepository.findAllByPageNumber(pageNumber);
    return mapper.toPageStatsResponse(pageNumber, stats, includeDetails);
  }

  /**
   * <p>Zwraca statystyki wszystkich stron telegazety w podanym zakresie dat.</p>
   * <p>Domyślnie (jeśli null):</p>
   * <ul>
   *   <li>fromDate = początek bieżącego miesiąca</li>
   *   <li>toDate = dzisiaj</li>
   * </ul>
   * <p>Statystyki są mapowane na {@link TeletextPageStatsResponse} i sortowane po wyświetleniach malejąco oraz numerze stron rosnąco</p>
   * <p>Paginacja jest wykonywana po posortowaniu w celu zachowania spójności danych.</p>
   *
   * @param pageable        obiekt Pageable do paginacji wyników
   * @param includeDetails  jeśli true — w DTO będą szczegóły dla każdej wizyty
   * @param fromDate        początkowa data filtrowania (inclusive), null = początek miesiąca
   * @param toDate          końcowa data filtrowania (inclusive), null = dzisiaj
   * @return lista {@link TeletextPageStatsResponse} dla wszystkich stron w podanym zakresie dat
   */
  public List<TeletextPageStatsResponse> getAllPagesStats(Pageable pageable, Boolean includeDetails, LocalDate fromDate, LocalDate toDate) {
    LocalDate today = LocalDate.now();
    fromDate = Objects.requireNonNullElse(fromDate, today.withDayOfMonth(1));
    toDate = Objects.requireNonNullElse(toDate, today);

    var stats = statsRepository.findAllStatsBetween(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));

    var result = mapper.toAllPageStatsResponse(stats, includeDetails).stream()
      .sorted(Comparator
        .comparingLong(TeletextPageStatsResponse::views).reversed()
        .thenComparingInt(TeletextPageStatsResponse::pageNumber))
      .toList();

    var firstIndex = pageable.getPageNumber() * pageable.getPageSize();
    var lastIndex = firstIndex + pageable.getPageSize();
    if(lastIndex > result.size()) lastIndex = result.size();

    return firstIndex >= result.size() ? List.of() : result.subList(firstIndex, lastIndex);
  }
}
