package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPage;

public interface TeletextPageRepository extends JpaRepository<TeletextPage, Long> {

  @Query("SELECT DISTINCT p FROM TeletextPage p LEFT JOIN FETCH p.content")
  List<TeletextPage> findAllWithContent();

  @Query("SELECT p FROM TeletextPage p LEFT JOIN FETCH p.content WHERE p.pageNumber = :pageNumber")
  Optional<TeletextPage> findByPageNumberWithContent(Integer pageNumber);
}
