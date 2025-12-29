package pl.studia.teletext.teletext_backend.teletext.page.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.common.exception.not_found.PageNotFoundException;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPageStats;
import pl.studia.teletext.teletext_backend.teletext.page.mapper.TeletextPageStatsMapper;
import pl.studia.teletext.teletext_backend.teletext.page.repository.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.teletext.page.repository.TeletextPageStatsRepository;
import pl.studia.teletext.teletext_backend.teletext.stats.api.dto.TeletextPageStatsResponse;

@Service
@RequiredArgsConstructor
public class TeletextPageStatsService {

  private final TeletextPageRepository pageRepository;
  private final TeletextPageStatsRepository statsRepository;
  private final TeletextPageStatsMapper mapper;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void recordPageVisit(Long pageId, Instant viewedAt) {
    var page =
        pageRepository
            .findById(pageId)
            .orElseThrow(
                () -> new PageNotFoundException("Strona o ID " + pageId + " nie znaleziona"));
    var stat = new TeletextPageStats(page, Timestamp.from(viewedAt));
    statsRepository.save(stat);
  }

  public TeletextPageStatsResponse getStatsForPage(Integer pageNumber, Boolean includeDetails) {
    var stats = statsRepository.findAllByPageNumber(pageNumber);
    return mapper.toPageStatsResponse(pageNumber, stats, includeDetails);
  }

  /**
   * Zwraca statystyki wszystkich stron telegazety w podanym zakresie dat.
   *
   * <p>Domyślnie (jeśli null):
   *
   * <ul>
   *   <li>fromDate = początek bieżącego miesiąca
   *   <li>toDate = dzisiaj
   * </ul>
   *
   * <p>Statystyki są mapowane na {@link TeletextPageStatsResponse} i sortowane po wyświetleniach
   * malejąco oraz numerze stron rosnąco
   *
   * <p>Paginacja jest wykonywana po posortowaniu w celu zachowania spójności danych.
   *
   * @param pageable obiekt Pageable do paginacji wyników
   * @param includeDetails jeśli true — w DTO będą szczegóły dla każdej wizyty
   * @param fromDate początkowa data filtrowania (inclusive), null = początek miesiąca
   * @param toDate końcowa data filtrowania (inclusive), null = dzisiaj
   * @return lista {@link TeletextPageStatsResponse} dla wszystkich stron w podanym zakresie dat
   */
  public List<TeletextPageStatsResponse> getAllPagesStats(
      Pageable pageable, Boolean includeDetails, LocalDate fromDate, LocalDate toDate) {
    LocalDate today = LocalDate.now();
    fromDate = Objects.requireNonNullElse(fromDate, today.withDayOfMonth(1));
    toDate = Objects.requireNonNullElse(toDate, today);

    var stats =
        statsRepository.findAllStatsBetween(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));

    var result =
        mapper.toAllPageStatsResponse(stats, includeDetails).stream()
            .sorted(
                Comparator.comparingLong(TeletextPageStatsResponse::views)
                    .reversed()
                    .thenComparingInt(TeletextPageStatsResponse::pageNumber))
            .toList();

    var firstIndex = pageable.getPageNumber() * pageable.getPageSize();
    var lastIndex = firstIndex + pageable.getPageSize();
    if (lastIndex > result.size()) lastIndex = result.size();

    return firstIndex >= result.size() ? List.of() : result.subList(firstIndex, lastIndex);
  }
}
