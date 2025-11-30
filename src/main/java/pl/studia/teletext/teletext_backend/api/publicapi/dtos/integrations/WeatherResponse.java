package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

public record WeatherResponse(String cityName, DailyWeather[] dailyWeathers) {
  public record DailyWeather(
      String date,
      double maxTemperature,
      String maxTemperatureUnit,
      double minTemperature,
      String minTemperatureUnit) {}
}
