package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageTemplate;

public interface TeletextPageTemplateRepository extends JpaRepository<TeletextPageTemplate, Long> {

  @Query("SELECT pt FROM TeletextPageTemplate  pt WHERE pt.deletedAt IS NULL")
  List<TeletextPageTemplate> findAllWithoutDeleted();

  @Query(
      "SELECT pt FROM TeletextPageTemplate pt WHERE pt.category = :category AND pt.deletedAt IS NULL")
  List<TeletextPageTemplate> findAllByCategory(TeletextCategory category);
}
