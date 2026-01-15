package pl.studia.teletext.teletext_backend.teletext.template.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.teletext.teletext_backend.common.exception.InvalidJsonConfigException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.SchemaNotFoundException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.TemplateNotFoundException;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextPageTemplateUpdateRequest;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPage;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;
import pl.studia.teletext.teletext_backend.teletext.schema.ConfigSchema;
import pl.studia.teletext.teletext_backend.teletext.schema.service.ConfigSchemaFactory;
import pl.studia.teletext.teletext_backend.teletext.template.domain.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.teletext.template.mapper.TeletextPageTemplateMapper;
import pl.studia.teletext.teletext_backend.teletext.template.repository.TeletextPageTemplateRepository;

// TODO: add TeletextPageStatsService tests after merging python tests branch.
@ExtendWith(MockitoExtension.class)
public class TeletextPageTemplateServiceTest {

  @Mock private TeletextPageTemplateRepository pageTemplateRepository;
  @Mock private ConfigSchemaFactory configSchemaFactory;
  @Mock private TeletextPageTemplateMapper teletextPageTemplateMapper;
  @InjectMocks private TeletextPageTemplateService teletextPageTemplateService;

  @Test
  void shouldReturnAllTemplatesForCategoryWithoutDeleted() {
    // given
    var includeInactive = false;
    var category = TeletextCategory.FINANCE;
    when(pageTemplateRepository.findAllByCategoryAndDeleted(eq(category), eq(includeInactive)))
        .thenReturn(List.of());

    // when
    var result = teletextPageTemplateService.getAllTemplates(category, includeInactive);

    // then
    assertNotNull(result);
    verify(pageTemplateRepository, times(1))
        .findAllByCategoryAndDeleted(eq(category), eq(includeInactive));
    verify(pageTemplateRepository, never()).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldReturnAllTemplatesForCategoryWithDeleted() {
    // given
    var includeInactive = true;
    var category = TeletextCategory.FINANCE;
    when(pageTemplateRepository.findAllByCategoryAndDeleted(eq(category), eq(includeInactive)))
        .thenReturn(List.of());

    // when
    var result = teletextPageTemplateService.getAllTemplates(category, includeInactive);

    // then
    assertNotNull(result);
    verify(pageTemplateRepository, times(1))
        .findAllByCategoryAndDeleted(eq(category), eq(includeInactive));
    verify(pageTemplateRepository, never()).findAllByDeleted(anyBoolean());
  }

  @Test
  void shouldReturnAllTemplatesWithoutDeleted() {
    // given
    var includeInactive = false;
    when(pageTemplateRepository.findAllByDeleted(eq(includeInactive))).thenReturn(List.of());

    // when
    var result = teletextPageTemplateService.getAllTemplates(null, includeInactive);

    // then
    assertNotNull(result);
    verify(pageTemplateRepository, never()).findAllByCategoryAndDeleted(any(), anyBoolean());
    verify(pageTemplateRepository, times(1)).findAllByDeleted(eq(includeInactive));
  }

  @Test
  void shouldReturnAllTemplatesWithDeleted() {
    // given
    var includeInactive = true;
    when(pageTemplateRepository.findAllByDeleted(eq(includeInactive))).thenReturn(List.of());

    // when
    var result = teletextPageTemplateService.getAllTemplates(null, includeInactive);

    // then
    assertNotNull(result);
    verify(pageTemplateRepository, never()).findAllByCategoryAndDeleted(any(), anyBoolean());
    verify(pageTemplateRepository, times(1)).findAllByDeleted(eq(includeInactive));
  }

  @Test
  void shouldReturnTemplateById() {
    // given
    var templateId = 1L;
    when(pageTemplateRepository.findByIdActive(eq(templateId)))
        .thenReturn(Optional.of(new TeletextPageTemplate()));

    // when
    var result = teletextPageTemplateService.getTemplateById(templateId);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowTemplateNotFoundExceptionWhenActiveTemplateNotFound() {
    // given
    var templateId = 1L;
    when(pageTemplateRepository.findByIdActive(eq(templateId))).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        TemplateNotFoundException.class,
        () -> teletextPageTemplateService.getTemplateById(templateId));
  }

  @Test
  void shouldCreateTemplate() {
    // given
    var source = TeletextSource.WEATHER;
    var configJson = new HashMap<String, Object>();
    var createRequest = createRequest(source, configJson);
    var template = template(source, configJson);
    var schema = mock(ConfigSchema.class);
    when(teletextPageTemplateMapper.toTemplate(eq(createRequest))).thenReturn(template);
    when(configSchemaFactory.getSchema(eq(source))).thenReturn(schema);
    when(pageTemplateRepository.save(eq(template))).thenReturn(new TeletextPageTemplate());

    // when
    var result = teletextPageTemplateService.createTemplate(createRequest);

    // then
    assertNotNull(result);
    verify(pageTemplateRepository, times(1)).save(any(TeletextPageTemplate.class));
    verify(schema, times(1)).validate(eq(configJson));
  }

  @Test
  void shouldThrowSchemaNotFoundExceptionWhenSchemaIsNotRegisteredWhileSavingTemplate() {
    // given
    var source = TeletextSource.WEATHER;
    var configJson = new HashMap<String, Object>();
    var createRequest = createRequest(source, configJson);
    var template = template(source, configJson);
    when(teletextPageTemplateMapper.toTemplate(eq(createRequest))).thenReturn(template);
    when(configSchemaFactory.getSchema(eq(source))).thenThrow(SchemaNotFoundException.class);

    // when & then
    assertThrows(
        SchemaNotFoundException.class,
        () -> teletextPageTemplateService.createTemplate(createRequest));
    verify(pageTemplateRepository, never()).save(any(TeletextPageTemplate.class));
  }

  @Test
  void shouldThrowInvalidJsonConfigExceptionWhenConfigJsonIsNotValid_Create() {
    // given
    var source = TeletextSource.WEATHER;
    var configJson = new HashMap<String, Object>();
    var createRequest = createRequest(source, configJson);
    var template = template(source, configJson);
    var schema = mock(ConfigSchema.class);
    when(teletextPageTemplateMapper.toTemplate(eq(createRequest))).thenReturn(template);
    when(configSchemaFactory.getSchema(eq(source))).thenReturn(schema);
    doThrow(InvalidJsonConfigException.class).when(schema).validate(eq(configJson));

    // when & then
    assertThrows(
        InvalidJsonConfigException.class,
        () -> teletextPageTemplateService.createTemplate(createRequest));
    verify(pageTemplateRepository, never()).save(any(TeletextPageTemplate.class));
  }

  @Test
  void shouldUpdateTemplate() {
    // given
    var id = 1L;
    var source = TeletextSource.EXCHANGE_RATE;
    var configJson = new HashMap<String, Object>();
    var updateRequest = updateRequest(source, configJson);
    var existingTemplate = template(source, configJson);
    var schema = mock(ConfigSchema.class);
    when(pageTemplateRepository.findByIdActive(eq(id))).thenReturn(Optional.of(existingTemplate));
    when(configSchemaFactory.getSchema(eq(source))).thenReturn(schema);
    when(pageTemplateRepository.save(eq(existingTemplate))).thenReturn(new TeletextPageTemplate());

    // when
    var result = teletextPageTemplateService.updateTemplate(id, updateRequest);

    // then
    assertNotNull(result);
    verify(schema, times(1)).validate(eq(configJson));
    verify(teletextPageTemplateMapper, times(1))
        .updateTemplateFromRequest(eq(updateRequest), eq(existingTemplate));
    verify(pageTemplateRepository, times(1)).save(eq(existingTemplate));
  }

  @Test
  void shouldThrowTemplateNotFoundExceptionWhenUpdatingNotExistingTemplate() {
    // given
    var id = 1L;
    var source = TeletextSource.EXCHANGE_RATE;
    var configJson = new HashMap<String, Object>();
    var updateRequest = updateRequest(source, configJson);
    when(pageTemplateRepository.findByIdActive(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        TemplateNotFoundException.class,
        () -> teletextPageTemplateService.updateTemplate(id, updateRequest));
    verify(pageTemplateRepository, never()).save(any(TeletextPageTemplate.class));
  }

  @Test
  void shouldThrowInvalidJsonConfigExceptionWhenConfigJsonIsNotValid_Update() {
    // given
    var id = 1L;
    var source = TeletextSource.EXCHANGE_RATE;
    var configJson = new HashMap<String, Object>();
    var updateRequest = updateRequest(source, configJson);
    var template = template(source, configJson);
    var schema = mock(ConfigSchema.class);
    when(pageTemplateRepository.findByIdActive(eq(id))).thenReturn(Optional.of(template));
    when(configSchemaFactory.getSchema(eq(source))).thenReturn(schema);
    doThrow(InvalidJsonConfigException.class).when(schema).validate(eq(configJson));

    // when & then
    assertThrows(
        InvalidJsonConfigException.class,
        () -> teletextPageTemplateService.updateTemplate(id, updateRequest));
    verify(pageTemplateRepository, never()).save(any(TeletextPageTemplate.class));
  }

  @Test
  void shouldActivateTemplate() {
    // given
    var id = 1L;
    var template = new TeletextPageTemplate();
    template.setDeletedAt(Timestamp.from(Instant.now()));
    when(pageTemplateRepository.findById(eq(id))).thenReturn(Optional.of(template));

    // when
    teletextPageTemplateService.activateTemplate(id);

    // then
    verify(pageTemplateRepository, times(1)).findById(eq(id));
    assertNull(template.getDeletedAt());
  }

  @Test
  void shouldThrowTemplateNotFoundExceptionWhenActivatingNonExistingTemplate() {
    // given
    var id = 1L;
    when(pageTemplateRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        TemplateNotFoundException.class, () -> teletextPageTemplateService.activateTemplate(id));
  }

  @Test
  void shouldThrowTemplateNotFoundExceptionWhenActivatingNotDeletedTemplate() {
    // given
    var id = 1L;
    var template = new TeletextPageTemplate();
    when(pageTemplateRepository.findById(eq(id))).thenReturn(Optional.of(template));

    // when & then
    assertThrows(
        TemplateNotFoundException.class, () -> teletextPageTemplateService.activateTemplate(id));
  }

  @Test
  void shouldDeactivateTemplate() {
    // given
    var id = 1L;
    var pages = List.of(new TeletextPage(), new TeletextPage());
    var template = new TeletextPageTemplate();
    template.setPages(pages);
    when(pageTemplateRepository.findByIdActive(eq(id))).thenReturn(Optional.of(template));

    // when
    teletextPageTemplateService.deactivateTemplate(id);

    // then
    verify(pageTemplateRepository, times(1)).findByIdActive(eq(id));
    assertNotNull(template.getDeletedAt());
    assertTrue(template.getPages().stream().allMatch(p -> p.getDeletedAt() != null));
  }

  @Test
  void shouldThrowTemplateNotFoundExceptionWhenDeactivatingNonExistingOrDeletedTemplate() {
    // given
    var id = 1L;
    when(pageTemplateRepository.findByIdActive(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        TemplateNotFoundException.class, () -> teletextPageTemplateService.deactivateTemplate(id));
  }

  private static TeletextPageTemplate template(
      TeletextSource source, Map<String, Object> configJson) {
    var template = new TeletextPageTemplate();
    template.setSource(source);
    template.setConfigJson(configJson);
    return template;
  }

  private static TeletextPageTemplateCreateRequest createRequest(
      TeletextSource source, HashMap<String, Object> configJson) {
    return new TeletextPageTemplateCreateRequest(
        "Test name", source, TeletextCategory.WEATHER, configJson);
  }

  private static TeletextPageTemplateUpdateRequest updateRequest(
      TeletextSource source, HashMap<String, Object> configJson) {
    return new TeletextPageTemplateUpdateRequest(
        "Updated name", source, TeletextCategory.FINANCE, configJson);
  }
}
