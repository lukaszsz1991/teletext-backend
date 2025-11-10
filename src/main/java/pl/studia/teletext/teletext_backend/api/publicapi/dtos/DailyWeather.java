package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

public record DailyWeather(
  String date,
  double maxTemperature,
  String maxTemperatureUnit,
  double minTemperature,
  String minTemperatureUnit
) { }
