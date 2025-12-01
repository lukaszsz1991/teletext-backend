package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;

public interface TeletextPageRepository extends JpaRepository<TeletextPage, Long> {

  @Query(
      "SELECT p FROM TeletextPage p LEFT JOIN FETCH p.content WHERE p.pageNumber = :pageNumber AND p.deletedAt IS NULL")
  Optional<TeletextPage> findByPageNumberWithContent(Integer pageNumber);

  @Query(
      "SELECT p FROM TeletextPage p LEFT JOIN FETCH p.content WHERE p.category = :category AND p.deletedAt IS NULL")
  List<TeletextPage> findByCategoryWithContent(TeletextCategory category);

  @Query("SELECT p FROM TeletextPage p WHERE p.deletedAt IS NULL")
  List<TeletextPage> findAll();

  @Query("SELECT p FROM TeletextPage p WHERE p.category = :category AND p.deletedAt IS NULL")
  List<TeletextPage> findAllByCategory(TeletextCategory category);
}
