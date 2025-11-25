package pl.studia.teletext.teletext_backend.clients.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DailyData(
  List<String> time,
  @JsonProperty("temperature_2m_max") List<Double> temperatureMax,
  @JsonProperty("temperature_2m_min") List<Double> temperatureMin
) { }
