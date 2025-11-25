package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TeletextHoroscopeResponse;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeResponse;

@Mapper(componentModel = "spring")
public interface TeletextHoroscopeMapper {

  TeletextHoroscopeResponse toResponse(HoroscopeResponse.SignInfo signInfo, LocalDate day);
}
