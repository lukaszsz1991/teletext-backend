package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.JobResponse;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleResponse;

@Mapper(componentModel = "spring")
public interface JobsMapper {

  @Mapping(target = "updatedAt", source = "job.updated")
  JobResponse toResponse(
      JoobleResponse.Job job,
      int totalResults,
      String searchKeywords,
      String searchLocation
  );
}
