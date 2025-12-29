package pl.studia.teletext.teletext_backend.integration.jooble.mapper;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.studia.teletext.teletext_backend.common.time.FlexibleDateParser;
import pl.studia.teletext.teletext_backend.integration.jooble.domain.JobResponse;
import pl.studia.teletext.teletext_backend.integration.jooble.domain.JoobleResponse;

@Mapper(componentModel = "spring")
public interface JobsMapper {

  @Mapping(target = "updatedAt", source = "job.updated", qualifiedByName = "mapStringToDate")
  JobResponse toResponse(
      JoobleResponse.Job job, int totalResults, String searchKeywords, String searchLocation);

  @Named("mapStringToDate")
  default LocalDateTime mapStringToDate(String date) {
    return FlexibleDateParser.parse(date);
  }
}
