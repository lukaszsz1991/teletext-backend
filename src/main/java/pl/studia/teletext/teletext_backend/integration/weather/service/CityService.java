package pl.studia.teletext.teletext_backend.integration.weather.service;

import java.text.Normalizer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.common.exception.not_found.CityNotFoundException;
import pl.studia.teletext.teletext_backend.integration.weather.domain.City;
import pl.studia.teletext.teletext_backend.integration.weather.repository.CityRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class CityService {

  private final CityRepository cityRepository;

  public List<City> getAllCities() {
    return cityRepository.getAllCities();
  }

  public City getCityByName(String name) {
    var normalizedInputName = normalizeCityName(name);
    return cityRepository.getAllCities().stream()
        .filter(
            c -> {
              var normalizedName = normalizeCityName(c.name());
              log.debug(
                  "Comparing with city: {} (normalized: {}, normalized input: {})",
                  c.name(),
                  normalizedName,
                  normalizedInputName);
              return normalizedInputName.equals(normalizedName);
            })
        .findFirst()
        .orElseThrow(() -> new CityNotFoundException("Nie znaleziono miasta " + name));
  }

  private String normalizeCityName(String name) {
    return Normalizer.normalize(name, Normalizer.Form.NFD)
        .trim()
        .toLowerCase()
        .replaceAll("\\p{M}", "")
        .replaceAll(" ", "-")
        .replaceAll("ł", "l");
  }
}
