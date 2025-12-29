package pl.studia.teletext.teletext_backend.teletext.page.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

@Component
public class TeletextSourceEnumConverter implements Converter<String, TeletextSource> {
  @Override
  public TeletextSource convert(String source) {
    return TeletextSource.fromString(source);
  }
}
