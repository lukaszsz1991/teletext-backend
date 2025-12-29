package pl.studia.teletext.teletext_backend.integration.tvp.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpChannel;

@Component
public class TvpChannelEnumConverter implements Converter<String, TvpChannel> {
  @Override
  public TvpChannel convert(String source) {
    return TvpChannel.fromString(source);
  }
}
