package pl.studia.teletext.teletext_backend.integration.weather.mapper;

import java.util.stream.IntStream;
import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.integration.weather.domain.DailyData;
import pl.studia.teletext.teletext_backend.integration.weather.domain.OpenMeteoResponse;
import pl.studia.teletext.teletext_backend.integration.weather.domain.WeatherResponse;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
  default WeatherResponse toWeatherResponse(OpenMeteoResponse response, String cityName) {
    WeatherResponse.DailyWeather[] dailyWeathers =
        mapDaily(response).toArray(WeatherResponse.DailyWeather[]::new);
    return new WeatherResponse(cityName, dailyWeathers);
  }

  default java.util.stream.Stream<WeatherResponse.DailyWeather> mapDaily(
      OpenMeteoResponse response) {
    DailyData daily = response.daily();
    var units = response.daily_units();
    return IntStream.range(0, daily.time().size())
        .mapToObj(
            i ->
                new WeatherResponse.DailyWeather(
                    daily.time().get(i),
                    daily.temperatureMax().get(i),
                    units.get("temperature_2m_max"),
                    daily.temperatureMin().get(i),
                    units.get("temperature_2m_min")));
  }
}
