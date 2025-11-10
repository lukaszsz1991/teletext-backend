package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

public record WeatherResponse(
  String cityName,
  DailyWeather[] dailyWeathers
) { }
