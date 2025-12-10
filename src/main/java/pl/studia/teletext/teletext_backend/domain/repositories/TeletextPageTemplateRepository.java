package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.TeletextPageTemplate;

public interface TeletextPageTemplateRepository extends JpaRepository<TeletextPageTemplate, Long> {

  @Query("SELECT pt FROM TeletextPageTemplate pt WHERE :includeInactive = true OR pt.deletedAt IS NULL")
  List<TeletextPageTemplate> findAllByDeleted(boolean includeInactive);

  @Query(
      "SELECT pt FROM TeletextPageTemplate pt WHERE pt.category = :category AND (:includeInactive = true OR pt.deletedAt IS NULL)")
  List<TeletextPageTemplate> findAllByCategoryAndDeleted(TeletextCategory category, boolean includeInactive);

  @Query(
      "SELECT pt FROM TeletextPageTemplate pt LEFT JOIN FETCH pt.pages WHERE pt.id = :id AND pt.deletedAt IS NULL")
  Optional<TeletextPageTemplate> findByIdActive(@Param("id") Long id);
}
