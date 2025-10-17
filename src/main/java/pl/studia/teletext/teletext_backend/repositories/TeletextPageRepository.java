package pl.studia.teletext.teletext_backend.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.models.TeletextPage;

public interface TeletextPageRepository extends JpaRepository<TeletextPage, Long> {

  @Query("SELECT DISTINCT p FROM TeletextPage p LEFT JOIN FETCH p.content")
  List<TeletextPage> findAllWithContent();
}
