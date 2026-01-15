package pl.studia.teletext.teletext_backend.teletext.schema.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.teletext.teletext_backend.common.exception.not_found.SchemaNotFoundException;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;
import pl.studia.teletext.teletext_backend.teletext.schema.ConfigSchema;
import pl.studia.teletext.teletext_backend.teletext.schema.validator.WeatherConfigSchema;

@ExtendWith(MockitoExtension.class)
public class TeletextSchemaServiceTest {

  @Mock private ConfigSchemaFactory configSchemaFactory;

  @InjectMocks private TeletextSchemaService teletextSchemaService;

  @Test
  void shouldReturnSchemaBySource() {
    // given
    var source = TeletextSource.WEATHER;
    when(configSchemaFactory.getSchema(source)).thenReturn(new WeatherConfigSchema());

    // when
    var result = teletextSchemaService.getSchema(source);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowSchemaNotFoundExceptionIfSchemaNotRegistered() {
    // given
    var source = TeletextSource.WEATHER;
    when(configSchemaFactory.getSchema(source)).thenThrow(SchemaNotFoundException.class);

    // when & then
    assertThrows(SchemaNotFoundException.class, () -> teletextSchemaService.getSchema(source));
  }

  @Test
  void shouldReturnAllSchemas() {
    // given
    when(configSchemaFactory.getSchema(any(TeletextSource.class)))
        .thenReturn(mock(ConfigSchema.class));

    // when
    var result = teletextSchemaService.getAllSchemas();

    // then
    assertEquals(TeletextSource.values().length, result.size());
  }

  @Test
  void shouldThrowSchemaNotFoundExceptionWhenGettingAllSchemasIfAnySchemaNotRegistered() {
    // given
    when(configSchemaFactory.getSchema(any(TeletextSource.class))).thenThrow(SchemaNotFoundException.class);

    // when & then
    assertThrows(SchemaNotFoundException.class, () -> teletextSchemaService.getAllSchemas());
  }
}
