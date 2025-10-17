package pl.studia.teletext.teletext_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.teletext.teletext_backend.models.TeletextPageContent;

public interface TeletextPageContentRepository extends JpaRepository<TeletextPageContent, Long> {
}
