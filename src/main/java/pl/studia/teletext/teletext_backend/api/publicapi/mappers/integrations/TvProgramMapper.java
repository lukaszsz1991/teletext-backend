package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TvProgramResponse;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface TvProgramMapper {

  @Mapping(target = "program", source = "tvpResponse.records")
  TvProgramResponse toResponse(String channelName, LocalDate date, TvpResponse tvpResponse);

  @Mapping(target = "time", source = "realDateTime")
  TvProgramResponse.ProgramSlot toProgramSlot(TvpResponse.PrRecord prRecord);

}
