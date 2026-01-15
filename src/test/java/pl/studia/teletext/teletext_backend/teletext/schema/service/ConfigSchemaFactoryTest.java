package pl.studia.teletext.teletext_backend.teletext.schema.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;
import pl.studia.teletext.teletext_backend.teletext.schema.validator.ExchangeRateConfigSchema;
import pl.studia.teletext.teletext_backend.teletext.schema.validator.NewsConfigSchema;

@ExtendWith(MockitoExtension.class)
public class ConfigSchemaFactoryTest {

  private ConfigSchemaFactory factory;

  @BeforeEach
  void setUp() {
    factory = new ConfigSchemaFactory();
  }

  @Test
  void shouldReturnExchangeRateConfigSchema() {
    // given
    var source = TeletextSource.EXCHANGE_RATE;

    // when
    var schema = factory.getSchema(source);

    // then
    assertNotNull(schema);
    assertInstanceOf(ExchangeRateConfigSchema.class, schema);
  }

  @Test
  void shouldReturnNewsConfigSchema() {
    // given
    var source = TeletextSource.NEWS;

    // when
    var schema = factory.getSchema(source);

    // then
    assertNotNull(schema);
    assertInstanceOf(NewsConfigSchema.class, schema);
  }
}
