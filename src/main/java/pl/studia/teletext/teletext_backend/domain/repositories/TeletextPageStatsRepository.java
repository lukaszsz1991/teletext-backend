package pl.studia.teletext.teletext_backend.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageStats;

public interface TeletextPageStatsRepository extends JpaRepository<TeletextPageStats, Long> {

  @Query(
      "SELECT s FROM TeletextPageStats s LEFT JOIN FETCH s.page p WHERE p.pageNumber = :pageNumber")
  List<TeletextPageStats> findAllByPageNumber(Integer pageNumber);

  @Query(
      "SELECT s FROM TeletextPageStats s LEFT JOIN FETCH s.page p WHERE s.openedAt >= :fromDate AND s.openedAt <= :toDate")
  List<TeletextPageStats> findAllStatsBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
