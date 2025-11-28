package pl.studia.teletext.teletext_backend.config.middleware.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;

@Component
public class HoroscopeEnumConverter implements Converter<String, HoroscopeSign> {
  @Override
  public HoroscopeSign convert(String source) {
    return HoroscopeSign.valueOf(source.toUpperCase());
  }
}
