package pl.studia.teletext.teletext_backend.integration.tvp.mapper;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvProgramResponse;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpResponse;

@Mapper(componentModel = "spring")
public interface TvProgramMapper {

  @Mapping(target = "program", source = "tvpResponse.records")
  TvProgramResponse toResponse(String channelName, LocalDate date, TvpResponse tvpResponse);

  @Mapping(target = "time", source = "realDateTime")
  TvProgramResponse.ProgramSlot toProgramSlot(TvpResponse.PrRecord prRecord);
}
