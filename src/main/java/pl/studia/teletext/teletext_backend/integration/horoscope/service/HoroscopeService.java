package pl.studia.teletext.teletext_backend.integration.horoscope.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.horoscope.client.HoroscopeClient;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeResponse;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeSign;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.TeletextHoroscopeResponse;
import pl.studia.teletext.teletext_backend.integration.horoscope.mapper.HoroscopeMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HoroscopeService {

  private final HoroscopeClient horoscopeClient;
  private final HoroscopeMapper horoscopeMapper;

  public Mono<TeletextHoroscopeResponse> getSingleSignHoroscope(
      HoroscopeSign sign, boolean forTomorrow) {
    Mono<HoroscopeResponse> source =
        forTomorrow ? horoscopeClient.getTomorrowHoroscope() : horoscopeClient.getTodayHoroscope();

    return source.flatMap(
        resp ->
            Mono.justOrEmpty(
                    resp.signs().stream()
                        .filter(s -> s.title().equalsIgnoreCase(sign.name()))
                        .findFirst())
                .map(h -> horoscopeMapper.toResponse(h, resp.day())));
  }
}
