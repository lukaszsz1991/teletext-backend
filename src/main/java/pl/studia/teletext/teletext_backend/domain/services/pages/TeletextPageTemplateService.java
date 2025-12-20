package pl.studia.teletext.teletext_backend.domain.services.pages;

import jakarta.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page_template.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page_template.TeletextPageTemplateUpdateRequest;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextPageTemplateMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageTemplateRepository;
import pl.studia.teletext.teletext_backend.exceptions.notfound.TemplateNotFoundException;

@Service
@RequiredArgsConstructor
public class TeletextPageTemplateService {

  private final TeletextPageTemplateRepository pageTemplateRepository;
  private final ConfigSchemaFactory configSchemaFactory;
  private final TeletextPageTemplateMapper teletextPageTemplateMapper;

  public List<TeletextPageTemplate> getAllTemplates(
      TeletextCategory category, boolean includeInactive) {
    return Optional.ofNullable(category)
        .map(c -> pageTemplateRepository.findAllByCategoryAndDeleted(c, includeInactive))
        .orElseGet(() -> pageTemplateRepository.findAllByDeleted(includeInactive));
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
    var template = teletextPageTemplateMapper.toTemplate(request);
    var schema = configSchemaFactory.getSchema(template.getSource());
    schema.validate(template.getConfigJson());
    return pageTemplateRepository.save(template);
  }

  @Transactional
  public TeletextPageTemplate updateTemplate(
      long id, @Valid TeletextPageTemplateUpdateRequest request) {
    var existingTemplate =
        pageTemplateRepository
            .findByIdActive(id)
            .orElseThrow(
                () ->
                    new TemplateNotFoundException("Szablon strony o ID: " + id + " nie istnieje"));

    teletextPageTemplateMapper.updateTemplateFromRequest(request, existingTemplate);
    var schema = configSchemaFactory.getSchema(existingTemplate.getSource());
    schema.validate(existingTemplate.getConfigJson());
    return pageTemplateRepository.save(existingTemplate);
  }

  @Transactional
  public void activateTemplate(Long id) {
    pageTemplateRepository
        .findById(id)
        .filter(t -> t.getDeletedAt() != null)
        .ifPresentOrElse(
            t -> t.setDeletedAt(null),
            () -> {
              throw new TemplateNotFoundException(
                  "Szablon strony o ID: " + id + " nie istnieje lub jest już aktywny");
            });
  }

  @Transactional
  public void deactivateTemplate(Long id) {
    pageTemplateRepository
        .findByIdActive(id)
        .ifPresentOrElse(
            t -> {
              t.getPages().forEach(p -> p.setDeletedAt(Timestamp.from(Instant.now())));
              t.setDeletedAt(Timestamp.from(Instant.now()));
            },
            () -> {
              throw new TemplateNotFoundException("Szablon strony o ID: " + id + " nie istnieje");
            });
  }
}
