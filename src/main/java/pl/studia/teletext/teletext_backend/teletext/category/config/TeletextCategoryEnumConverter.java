package pl.studia.teletext.teletext_backend.teletext.category.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;

@Component
public class TeletextCategoryEnumConverter implements Converter<String, TeletextCategory> {
  @Override
  public TeletextCategory convert(String source) {
    return TeletextCategory.fromString(source);
  }
}
