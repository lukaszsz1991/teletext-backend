package pl.studia.teletext.teletext_backend.integration.horoscope.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeSign;

@Component
public class HoroscopeEnumConverter implements Converter<String, HoroscopeSign> {
  @Override
  public HoroscopeSign convert(String source) {
    return HoroscopeSign.fromString(source);
  }
}
