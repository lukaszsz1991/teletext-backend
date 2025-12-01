package pl.studia.teletext.teletext_backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageContent;

public interface TeletextPageContentRepository extends JpaRepository<TeletextPageContent, Long> {}
