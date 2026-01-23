package pl.studia.teletext.teletext_backend.integration.tvp.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import pl.studia.teletext.teletext_backend.common.utils.time.FlexibleDateParser;

public class JaxbLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

  @Override
  public LocalDateTime unmarshal(String value) {
    return FlexibleDateParser.parse(value);
  }

  @Override
  public String marshal(LocalDateTime value) {
    return FlexibleDateParser.format(value);
  }
}
