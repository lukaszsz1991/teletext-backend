package pl.studia.teletext.teletext_backend.clients.weather;

import java.util.Map;

public record OpenMeteoResponse(
  double latitude,
  double longitude,
  Map<String, String> daily_units,
  DailyData daily
) {}
