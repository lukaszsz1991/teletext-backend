package pl.studia.teletext.teletext_backend.config.middleware.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;

@Component
public class TeletextCategoryEnumConverter implements Converter<String, TeletextCategory> {
  @Override
  public TeletextCategory convert(String source) {
    return TeletextCategory.fromString(source);
  }
}
