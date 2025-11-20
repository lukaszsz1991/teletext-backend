package pl.studia.teletext.teletext_backend.domain.services.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TeletextHoroscopeResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.TeletextHoroscopeMapper;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeClient;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeResponse;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HoroscopeService {

  private final HoroscopeClient horoscopeClient;
  private final TeletextHoroscopeMapper horoscopeMapper;

  public Mono<TeletextHoroscopeResponse> getSingleSignHoroscope(HoroscopeSign sign, boolean forTomorrow) {
    Mono<HoroscopeResponse> source = forTomorrow ?
      horoscopeClient.getTomorrowHoroscope() :
      horoscopeClient.getTodayHoroscope();

    return source.flatMap(
      resp -> Mono.justOrEmpty(resp.signs().stream()
          .filter(s -> s.title().equalsIgnoreCase(sign.name()))
          .findFirst())
        .map(h -> horoscopeMapper.toResponse(h, resp.day())));
  }
}
