package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.JobResponse;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleResponse;

@Component
public class JobsExternalDataMapper implements ExternalDataMapper<JobResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(JobResponse source) {
    return new ExternalDataResponse(
      "job-offers",
      source.title(),
      source.snippet(),
      toAdditionalData(source)
    );
  }

  @Override
  public Map<String, Object> toAdditionalData(JobResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("totalResults", source.totalResults());
    info.put("searchKeywords", source.searchKeywords());
    info.put("searchLocation", source.searchLocation());
    info.put("location", source.location());
    info.put("salary", source.salary());
    info.put("company", source.company());
    info.put("updatedAt", source.updatedAt());
    return info;
  }
}
