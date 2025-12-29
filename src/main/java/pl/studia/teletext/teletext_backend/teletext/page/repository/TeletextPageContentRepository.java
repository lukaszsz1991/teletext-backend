package pl.studia.teletext.teletext_backend.teletext.page.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPageContent;

public interface TeletextPageContentRepository extends JpaRepository<TeletextPageContent, Long> {}
