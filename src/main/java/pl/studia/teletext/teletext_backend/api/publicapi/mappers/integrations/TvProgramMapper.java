package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TvProgramResponse;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface TvProgramMapper {

  @Mapping(target = "program", source = "tvpResponse.records")
  TvProgramResponse toResponse(String channelName, LocalDate date, TvpResponse tvpResponse);

  @Mapping(target = "time", source = "realDateTime", qualifiedByName = "mapToTime")
  TvProgramResponse.ProgramSlot toProgramSlot(TvpResponse.PrRecord prRecord);

  @Named("mapToTime")
  default LocalTime mapToTime(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.toLocalTime();
  }
}
