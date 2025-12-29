package pl.studia.teletext.teletext_backend.integration.tvp.mapper;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.common.dto.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.common.mapper.ExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvProgramResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

@Component
public class TvProgramExternalDataMapper implements ExternalDataMapper<TvProgramResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(TvProgramResponse source) {
    return new ExternalDataResponse(
        TeletextSource.TV_PROGRAM,
        "Program " + source.channelName(),
        "Program TV na dzień " + source.date(),
        toAdditionalData(source));
  }

  @Override
  public Map<String, Object> toAdditionalData(TvProgramResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("channelName", source.channelName());
    info.put("date", source.date().toString());
    info.put("program", source.program());
    return info;
  }
}
