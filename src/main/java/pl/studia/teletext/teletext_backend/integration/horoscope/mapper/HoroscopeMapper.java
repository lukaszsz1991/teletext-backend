package pl.studia.teletext.teletext_backend.integration.horoscope.mapper;

import java.time.LocalDate;
import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeResponse;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.TeletextHoroscopeResponse;

@Mapper(componentModel = "spring")
public interface HoroscopeMapper {

  TeletextHoroscopeResponse toResponse(HoroscopeResponse.SignInfo signInfo, LocalDate day);
}
