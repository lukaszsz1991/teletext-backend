package pl.studia.teletext.teletext_backend.domain.services.pages;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.api.admin.dtos.ConfigSchemaResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextPageTemplateMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageTemplateRepository;
import pl.studia.teletext.teletext_backend.exceptions.TemplateNotFoundException;

@Service
@RequiredArgsConstructor
public class TeletextPageTemplateService {

  private final TeletextPageTemplateRepository pageTemplateRepository;
  private final ConfigSchemaFactory configSchemaFactory;
  private final TeletextPageTemplateMapper teletextPageTemplateMapper;

  public List<TeletextPageTemplate> getAllTemplates(TeletextCategory category) {
    return Optional.ofNullable(category)
        .map(pageTemplateRepository::findAllByCategory)
        .orElseGet(pageTemplateRepository::findAllWithoutDeleted);
  }

  @Transactional(readOnly = true)
  public TeletextPageTemplate getTemplateById(Long id) {
    return pageTemplateRepository
        .findByIdActive(id)
        .orElseThrow(
            () -> new TemplateNotFoundException("Szablon strony o podanym ID nie istnieje"));
  }

  @Transactional
  public TeletextPageTemplate createTemplate(@Valid TeletextPageTemplateCreateRequest request) {
    var schema = configSchemaFactory.getSchema(request.source());
    schema.validate(request.configJson());
    var template = teletextPageTemplateMapper.toTemplate(request);
    return pageTemplateRepository.save(template);
  }
}
