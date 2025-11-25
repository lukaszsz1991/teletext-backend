package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.List;
import pl.studia.teletext.teletext_backend.domain.models.City;

public interface CityRepository {
  List<City> getAllCities();
}
