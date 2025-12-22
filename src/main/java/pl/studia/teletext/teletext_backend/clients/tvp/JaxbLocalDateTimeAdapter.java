package pl.studia.teletext.teletext_backend.clients.tvp;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import pl.studia.teletext.teletext_backend.domain.services.FlexibleDateParser;

import java.time.LocalDateTime;

public class JaxbLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

  @Override
  public LocalDateTime unmarshal(String value) {
    return FlexibleDateParser.parse(value);
  }

  @Override
  public String marshal(LocalDateTime value) {
    return FlexibleDateParser.parse(value);
  }
}
