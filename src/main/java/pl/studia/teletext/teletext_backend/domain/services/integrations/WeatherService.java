package pl.studia.teletext.teletext_backend.domain.services.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.WeatherResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.WeatherMapper;
import pl.studia.teletext.teletext_backend.clients.weather.OpenMeteoClient;
import pl.studia.teletext.teletext_backend.domain.services.CityService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

  private final OpenMeteoClient openMeteoClient;
  private final CityService cityService;
  private final WeatherMapper weatherMapper;

  public Flux<WeatherResponse> getWeeklyWeatherForAllCities() {
    var cities = cityService.getAllCities();
    return Flux.fromIterable(cities)
        .flatMap(
            city ->
                openMeteoClient
                    .getWeeklyWeather(city.latitude(), city.longitude())
                    .map(response -> weatherMapper.toWeatherResponse(response, city.name())));
  }

  public Mono<WeatherResponse> getWeeklyWeatherForCity(String cityName) {
    var city = cityService.getCityByName(cityName);
    return openMeteoClient
        .getWeeklyWeather(city.latitude(), city.longitude())
        .map(response -> weatherMapper.toWeatherResponse(response, city.name()));
  }
}
