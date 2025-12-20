package pl.studia.teletext.teletext_backend.domain.services.pages;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.*;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextAdminPageMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.events.stats.PageViewedEvent;
import pl.studia.teletext.teletext_backend.exceptions.notfound.PageNotFoundException;

@Service
@RequiredArgsConstructor
public class TeletextPageService {

  private final TeletextPageTemplateService templateService;
  private final TeletextPageGeneratorService pageGeneratorService;
  private final TeletextPageRepository teletextPageRepository;
  private final TeletextPageMapper mapper;
  private final TeletextAdminPageMapper adminMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional(readOnly = true)
  public TeletextDetailedPageResponse viewPageByPageNumber(int pageNumber) {
    var page = getPageWithContentByPageNumber(pageNumber);
    eventPublisher.publishEvent(new PageViewedEvent(page.getId(), Instant.now()));
    return mapper.toDetailedPageResponse(page);
  }

  private TeletextPage getPageWithContentByPageNumber(int pageNumber) {
    var page =
        teletextPageRepository
            .findByPageNumberWithContent(pageNumber)
            .orElseThrow(
                () -> new PageNotFoundException("Nie odnaleziono strony o numerze " + pageNumber));

    return page.getTemplate() != null
        ? pageGeneratorService.generatePageFromTemplate(page).block()
        : page;
  }

  @Cacheable(value = "pages", key = "#id", unless = "#result == null")
  public TeletextAdminPageResponse getPageWithContentById(Long id) {
    var page =
        teletextPageRepository
            .findByIdWithContent(id)
            .orElseThrow(() -> new PageNotFoundException("Nie odnaleziono strony o id " + id));

    var result =
        page.getTemplate() != null
            ? pageGeneratorService.generatePageFromTemplate(page).block()
            : page;

    return adminMapper.toResponse(result);
  }

  public List<TeletextPageResponse> getPagesByCategory(TeletextCategory category, String title) {
    String titleNormalized = (title == null) ? "" : title.toLowerCase().trim();
    return teletextPageRepository.findByCategoryWithContent(category).stream()
        .filter(
            page -> {
              if (titleNormalized.isBlank()) return true;
              var contentTitle =
                  page.getContent() != null ? page.getContent().getTitle().toLowerCase() : "";
              var templateTitle =
                  page.getTemplate() != null ? page.getTemplate().getName().toLowerCase() : "";
              return contentTitle.contains(titleNormalized)
                  || templateTitle.contains(titleNormalized);
            })
        .map(mapper::toPageResponse)
        .sorted(Comparator.comparing(TeletextPageResponse::pageNumber))
        .toList();
  }

  public List<TeletextPageResponse> getAllPages(
      TeletextCategory category, boolean includeInactive) {
    return Optional.ofNullable(category)
        .map(c -> teletextPageRepository.findAllByCategoryAndDeleted(c, includeInactive))
        .orElseGet(() -> teletextPageRepository.findAllByDeleted(includeInactive))
        .stream()
        .map(mapper::toPageResponse)
        .toList();
  }

  @Transactional
  public TeletextAdminPageResponse createPage(PageCreateRequest request) {
    return request.handle(this);
  }

  @CachePut(value = "pages", key = "#result.id", condition = "#result != null")
  public TeletextAdminPageResponse createManualPage(ManualPageCreateRequest request) {
    var page = adminMapper.toPage(request);
    var saved = teletextPageRepository.save(page);
    return adminMapper.toResponse(saved);
  }

  @CachePut(value = "pages", key = "#result.id", condition = "#result != null")
  public TeletextAdminPageResponse createTemplatePage(TemplatePageCreateRequest request) {
    var page = adminMapper.toPage(request);
    var template = templateService.getTemplateById(request.templateId());
    page.setTemplate(template);
    var saved = teletextPageRepository.save(page);
    return adminMapper.toResponse(saved);
  }

  @Transactional
  public TeletextAdminPageResponse updatePage(Long id, PageUpdateRequest request) {
    return request.handle(id, this);
  }

  @CachePut(value = "pages", key = "#result.id", condition = "#result != null")
  public TeletextAdminPageResponse updateManualPage(Long id, ManualPageUpdateRequest request) {
    var page =
        teletextPageRepository
            .findByIdWithContent(id)
            .orElseThrow(() -> new PageNotFoundException("Nie odnaleziono strony o id " + id));
    adminMapper.updatePageFromManualRequest(request, page);
    page = teletextPageRepository.save(page);
    return adminMapper.toResponse(page);
  }

  @CachePut(value = "pages", key = "#result.id", condition = "#result != null")
  public TeletextAdminPageResponse updateTemplatePage(Long id, TemplatePageUpdateRequest request) {
    var page =
        teletextPageRepository
            .findById(id)
            .orElseThrow(() -> new PageNotFoundException("Nie odnaleziono strony o id " + id));
    var template = templateService.getTemplateById(request.templateId());
    page.setTemplate(template);
    adminMapper.updatePageFromTemplateRequest(request, page);
    page = teletextPageRepository.save(page);
    return adminMapper.toResponse(page);
  }

  @Transactional
  @CacheEvict(value = "pages", key = "#id")
  public void activatePage(Long id) {
    teletextPageRepository
        .findById(id)
        .filter(page -> page.getDeletedAt() != null)
        .ifPresentOrElse(
            page -> page.setDeletedAt(null),
            () -> {
              throw new PageNotFoundException(
                  "Strona o ID: " + id + " nie istnieje lub jest już aktywna");
            });
  }

  @Transactional
  @CacheEvict(value = "pages", key = "#id")
  public void deactivatePage(Long id) {
    teletextPageRepository
        .findById(id)
        .filter(page -> page.getDeletedAt() == null)
        .ifPresentOrElse(
            page -> page.setDeletedAt(Timestamp.from(Instant.now())),
            () -> {
              throw new PageNotFoundException("Strona o ID: " + id + " nie istnieje");
            });
  }
}
