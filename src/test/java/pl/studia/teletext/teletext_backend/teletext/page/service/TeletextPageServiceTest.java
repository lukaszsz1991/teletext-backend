package pl.studia.teletext.teletext_backend.teletext.page.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pl.studia.teletext.teletext_backend.common.exception.not_found.PageNotFoundException;
import pl.studia.teletext.teletext_backend.events.stats.PageViewedEvent;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.*;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPage;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPageContent;
import pl.studia.teletext.teletext_backend.teletext.page.mapper.TeletextAdminPageMapper;
import pl.studia.teletext.teletext_backend.teletext.page.mapper.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.teletext.page.repository.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.teletext.template.domain.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.teletext.template.service.TeletextPageGeneratorService;
import pl.studia.teletext.teletext_backend.teletext.template.service.TeletextPageTemplateService;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class TeletextPageServiceTest {

  private List<TeletextPage> pages;
  @Mock private TeletextPageRepository teletextPageRepository;
  @Mock private TeletextPageGeneratorService pageGeneratorService;
  @Mock private ApplicationEventPublisher eventPublisher;
  @Mock private TeletextPageMapper publicMapper;
  @Mock private TeletextAdminPageMapper adminMapper;
  @Mock private TeletextPageTemplateService templateService;
  @InjectMocks private TeletextPageService teletextPageService;

  @BeforeEach
  void setUp() {
    pages = new ArrayList<>();
    pages.add(page(101, TeletextCategory.NEWS, null, "News Title 1"));
    pages.add(page(102, TeletextCategory.NEWS, "News Title 2", null));
    pages.add(page(103, TeletextCategory.NEWS, null, "Another Title 3"));
  }

  @Test
  void shouldShowManualPageByPageNumber() {
    // given
    var pageNumber = 101;
    var page = new TeletextPage();
    page.setTemplate(null);
    when(teletextPageRepository.findByPageNumberWithContent(eq(pageNumber)))
        .thenReturn(Optional.of(page));
    when(publicMapper.toDetailedPageResponse(eq(page)))
        .thenReturn(mock(TeletextDetailedPageResponse.class));

    // when
    var result = teletextPageService.viewPageByPageNumber(pageNumber);

    // then
    assertNotNull(result);
    verify(eventPublisher, times(1)).publishEvent(any(PageViewedEvent.class));
    verify(pageGeneratorService, never()).generatePageFromTemplate(any(TeletextPage.class));
  }

  @Test
  void shouldShowTemplateBasedPageByPageNumber() {
    // given
    var pageNumber = 101;
    var page = new TeletextPage();
    page.setTemplate(new TeletextPageTemplate());
    var generatedPage = new TeletextPage();
    when(teletextPageRepository.findByPageNumberWithContent(eq(pageNumber)))
        .thenReturn(Optional.of(page));
    when(pageGeneratorService.generatePageFromTemplate(eq(page)))
        .thenReturn(Mono.just(generatedPage));
    when(publicMapper.toDetailedPageResponse(eq(generatedPage)))
        .thenReturn(mock(TeletextDetailedPageResponse.class));

    // when
    var result = teletextPageService.viewPageByPageNumber(pageNumber);

    // then
    assertNotNull(result);
    verify(publicMapper).toDetailedPageResponse(eq(generatedPage));
    verify(eventPublisher, times(1)).publishEvent(any(PageViewedEvent.class));
    verify(pageGeneratorService, times(1)).generatePageFromTemplate(eq(page));
  }

  @Test
  void shouldThrowPageNotFoundExceptionWhileViewedPageIsNotPresentOrDeleted() {
    // given
    var pageNumber = 101;
    when(teletextPageRepository.findByPageNumberWithContent(eq(pageNumber)))
        .thenReturn(Optional.empty());

    // when & then
    assertThrows(
        PageNotFoundException.class, () -> teletextPageService.viewPageByPageNumber(pageNumber));
    verify(eventPublisher, never()).publishEvent(any(PageViewedEvent.class));
  }

  @Test
  void shouldReturnManualPageWithContentById() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setTemplate(null);
    when(teletextPageRepository.findByIdWithContent(eq(id))).thenReturn(Optional.of(page));
    when(adminMapper.toResponse(eq(page))).thenReturn(mock(TeletextAdminPageResponse.class));

    // when
    var result = teletextPageService.getPageWithContentById(id);

    // then
    assertNotNull(result);
    verify(pageGeneratorService, never()).generatePageFromTemplate(any(TeletextPage.class));
  }

  @Test
  void shouldReturnTemplateBasedPageWithContentById() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setTemplate(new TeletextPageTemplate());
    var generatedPage = new TeletextPage();
    when(teletextPageRepository.findByIdWithContent(eq(id))).thenReturn(Optional.of(page));
    when(pageGeneratorService.generatePageFromTemplate(eq(page)))
        .thenReturn(Mono.just(generatedPage));
    when(adminMapper.toResponse(eq(generatedPage)))
        .thenReturn(mock(TeletextAdminPageResponse.class));

    // when
    var result = teletextPageService.getPageWithContentById(id);

    // then
    assertNotNull(result);
    verify(adminMapper).toResponse(eq(generatedPage));
    verify(pageGeneratorService, times(1)).generatePageFromTemplate(eq(page));
  }

  @Test
  void shouldThrowPageNotFoundExceptionWhenPageWithContentByIdIsNotPresent() {
    // given
    var id = 1L;
    when(teletextPageRepository.findByIdWithContent(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(PageNotFoundException.class, () -> teletextPageService.getPageWithContentById(id));
  }

  @Test
  void shouldReturnPagesByCategoryWithNormalizedTitle() {
    // given
    var category = TeletextCategory.NEWS;
    var title = "            NEWS title    ";
    when(teletextPageRepository.findByCategoryWithContent(eq(category))).thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getPagesByCategory(category, title);

    // then
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void shouldReturnPagesByCategoryWithBlankTitle() {
    // given
    var category = TeletextCategory.NEWS;
    var title = "        ";
    when(teletextPageRepository.findByCategoryWithContent(eq(category))).thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getPagesByCategory(category, title);

    // then
    assertNotNull(result);
    assertEquals(3, result.size());
  }

  @Test
  void shouldReturnEmptyPagesByCategoryWhenNotFound() {
    // given
    var category = TeletextCategory.SPORTS;
    var title = "        ";
    when(teletextPageRepository.findByCategoryWithContent(eq(category))).thenReturn(List.of());

    // when
    var result = teletextPageService.getPagesByCategory(category, title);

    // then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnAllPagesByCategoryIncludingInactive() {
    // given
    var category = TeletextCategory.NEWS;
    var includeInactive = true;
    when(teletextPageRepository.findAllByCategoryAndDeleted(eq(category), eq(includeInactive)))
        .thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getAllPages(category, includeInactive);

    // then
    assertNotNull(result);
    assertEquals(3, result.size());
    verify(teletextPageRepository, times(1))
        .findAllByCategoryAndDeleted(eq(category), eq(includeInactive));
    verify(teletextPageRepository, never()).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldReturnAllPagesByCategoryWithoutInactive() {
    // given
    var category = TeletextCategory.NEWS;
    var includeInactive = false;
    when(teletextPageRepository.findAllByCategoryAndDeleted(eq(category), eq(includeInactive)))
        .thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getAllPages(category, includeInactive);

    // then
    assertNotNull(result);
    assertEquals(3, result.size());
    verify(teletextPageRepository, times(1))
        .findAllByCategoryAndDeleted(eq(category), eq(includeInactive));
    verify(teletextPageRepository, never()).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldReturnAllPagesIncludingInactive() {
    // given
    var includeInactive = true;
    when(teletextPageRepository.findAllByDeleted(eq(includeInactive))).thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getAllPages(null, includeInactive);

    // then
    assertNotNull(result);
    assertEquals(3, result.size());
    verify(teletextPageRepository, never())
        .findAllByCategoryAndDeleted(any(TeletextCategory.class), anyBoolean());
    verify(teletextPageRepository, times(1)).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldReturnAllPagesWithoutInactive() {
    // given
    var includeInactive = false;
    when(teletextPageRepository.findAllByDeleted(eq(includeInactive))).thenReturn(pages);
    when(publicMapper.toPageResponse(any(TeletextPage.class)))
        .thenReturn(mock(TeletextPageResponse.class));

    // when
    var result = teletextPageService.getAllPages(null, includeInactive);

    // then
    assertNotNull(result);
    assertEquals(3, result.size());
    verify(teletextPageRepository, never())
        .findAllByCategoryAndDeleted(any(TeletextCategory.class), anyBoolean());
    verify(teletextPageRepository, times(1)).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldCreateManualPage() {
    // given
    var request = mock(ManualPageCreateRequest.class);
    var page = new TeletextPage();
    var savedPage = new TeletextPage();
    var response = mock(TeletextAdminPageResponse.class);
    when(adminMapper.toPage(eq(request))).thenReturn(page);
    when(teletextPageRepository.save(eq(page))).thenReturn(savedPage);
    when(adminMapper.toResponse(eq(savedPage))).thenReturn(response);

    // when
    var result = teletextPageService.createManualPage(request);

    // then
    assertNotNull(result);
    assertEquals(response, result);
  }

  @Test
  void shouldCreateTemplatePage() {
    // given
    var templateId = 10L;
    var request = mock(TemplatePageCreateRequest.class);
    var page = new TeletextPage();
    var template = new TeletextPageTemplate();
    var savedPage = new TeletextPage();
    var response = mock(TeletextAdminPageResponse.class);
    when(request.templateId()).thenReturn(templateId);
    when(adminMapper.toPage(eq(request))).thenReturn(page);
    when(templateService.getTemplateById(eq(templateId))).thenReturn(template);
    when(teletextPageRepository.save(eq(page))).thenReturn(savedPage);
    when(adminMapper.toResponse(eq(savedPage))).thenReturn(response);

    // when
    var result = teletextPageService.createTemplatePage(request);

    // then
    assertNotNull(result);
    assertEquals(template, page.getTemplate());
  }

  @Test
  void shouldUpdateManualPage() {
    // given
    var id = 1L;
    var request = mock(ManualPageUpdateRequest.class);
    var page = new TeletextPage();
    var savedPage = new TeletextPage();
    var response = mock(TeletextAdminPageResponse.class);

    when(teletextPageRepository.findByIdWithContent(eq(id))).thenReturn(Optional.of(page));
    when(teletextPageRepository.save(eq(page))).thenReturn(savedPage);
    when(adminMapper.toResponse(eq(savedPage))).thenReturn(response);

    // when
    var result = teletextPageService.updateManualPage(id, request);

    // then
    assertNotNull(result);
    verify(adminMapper).updatePageFromManualRequest(eq(request), eq(page));
  }

  @Test
  void shouldThrowExceptionWhenUpdateManualPageNotFound() {
    // given
    var id = 1L;
    var request = mock(ManualPageUpdateRequest.class);
    when(teletextPageRepository.findByIdWithContent(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        PageNotFoundException.class, () -> teletextPageService.updateManualPage(id, request));
  }

  @Test
  void shouldUpdateTemplatePage() {
    // given
    var id = 1L;
    var templateId = 5L;
    var request = mock(TemplatePageUpdateRequest.class);
    var page = new TeletextPage();
    var template = new TeletextPageTemplate();
    var savedPage = new TeletextPage();
    var response = mock(TeletextAdminPageResponse.class);

    when(request.templateId()).thenReturn(templateId);
    when(teletextPageRepository.findById(eq(id))).thenReturn(Optional.of(page));
    when(templateService.getTemplateById(eq(templateId))).thenReturn(template);
    when(teletextPageRepository.save(eq(page))).thenReturn(savedPage);
    when(adminMapper.toResponse(eq(savedPage))).thenReturn(response);

    // when
    var result = teletextPageService.updateTemplatePage(id, request);

    // then
    assertNotNull(result);
    assertEquals(template, page.getTemplate());
    verify(adminMapper).updatePageFromTemplateRequest(eq(request), eq(page));
  }

  @Test
  void shouldActivatePage() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setDeletedAt(Timestamp.from(Instant.now()));

    when(teletextPageRepository.findById(eq(id))).thenReturn(Optional.of(page));

    // when
    teletextPageService.activatePage(id);

    // then
    assertNull(page.getDeletedAt());
  }

  @Test
  void shouldThrowExceptionWhenActivatePageAlreadyActive() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setDeletedAt(null);

    when(teletextPageRepository.findById(eq(id))).thenReturn(Optional.of(page));

    // when & then
    assertThrows(PageNotFoundException.class, () -> teletextPageService.activatePage(id));
  }

  @Test
  void shouldDeactivatePage() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setDeletedAt(null);

    when(teletextPageRepository.findById(eq(id))).thenReturn(Optional.of(page));

    // when
    teletextPageService.deactivatePage(id);

    // then
    assertNotNull(page.getDeletedAt());
  }

  @Test
  void shouldThrowExceptionWhenDeactivatePageNotFound() {
    // given
    var id = 1L;
    var page = new TeletextPage();
    page.setDeletedAt(Timestamp.from(Instant.now()));

    when(teletextPageRepository.findById(eq(id))).thenReturn(Optional.of(page));

    // when & then
    assertThrows(PageNotFoundException.class, () -> teletextPageService.deactivatePage(id));
  }

  private TeletextPage page(
      int pageNumber, TeletextCategory category, String contentTitle, String templateName) {
    var page = new TeletextPage();
    page.setId((long) (pages.size() + 1));
    page.setPageNumber(pageNumber);
    page.setCategory(category);
    if (contentTitle != null) {
      var content = new TeletextPageContent();
      content.setTitle(contentTitle);
      page.setContent(content);
    } else if (templateName != null) {
      var template = new TeletextPageTemplate();
      template.setName(templateName);
      page.setTemplate(template);
    }
    return page;
  }
}
