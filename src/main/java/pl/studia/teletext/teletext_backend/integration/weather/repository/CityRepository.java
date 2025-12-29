package pl.studia.teletext.teletext_backend.integration.weather.repository;

import java.util.List;
import pl.studia.teletext.teletext_backend.integration.weather.domain.City;

public interface CityRepository {
  List<City> getAllCities();
}
