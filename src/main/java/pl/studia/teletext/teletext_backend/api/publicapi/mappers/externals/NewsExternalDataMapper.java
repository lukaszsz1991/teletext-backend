package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.NewsResponse;

@Component
public class NewsExternalDataMapper implements ExternalDataMapper<NewsResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(NewsResponse source) {
    return new ExternalDataResponse(
        "news", source.title(), source.description(), toAdditionalData(source));
  }

  @Override
  public Map<String, Object> toAdditionalData(NewsResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("link", source.link());
    info.put("keywords", source.keywords());
    info.put("creator", source.creator());
    info.put("country", source.country());
    info.put("category", source.category());
    info.put("publicationDate", source.pubDate());
    info.put("sourceName", source.sourceName());
    info.put("sourceUrl", source.sourceUrl());
    return info;
  }
}
