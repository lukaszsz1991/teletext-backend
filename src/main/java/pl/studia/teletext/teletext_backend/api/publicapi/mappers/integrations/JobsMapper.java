package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.JobResponse;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleResponse;
import pl.studia.teletext.teletext_backend.domain.services.FlexibleDateParser;

@Mapper(componentModel = "spring")
public interface JobsMapper {

  @Mapping(target = "updatedAt", source = "job.updated", qualifiedByName = "mapStringToDate")
  JobResponse toResponse(
      JoobleResponse.Job job,
      int totalResults,
      String searchKeywords,
      String searchLocation
  );

  @Named("mapStringToDate")
  default LocalDateTime mapStringToDate(String date) {
    return FlexibleDateParser.parse(date);
  }
}
