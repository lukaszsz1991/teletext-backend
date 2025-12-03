package pl.studia.teletext.teletext_backend.domain.services.pages;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageTemplateRepository;

@Service
@RequiredArgsConstructor
public class TeletextPageTemplateService {

  private final TeletextPageTemplateRepository pageTemplateRepository;

  public List<TeletextPageTemplate> getAllTemplates(TeletextCategory category) {
    return Optional.ofNullable(category)
        .map(pageTemplateRepository::findAllByCategory)
        .orElseGet(pageTemplateRepository::findAllWithoutDeleted);
  }
}
