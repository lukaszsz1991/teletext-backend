package pl.studia.teletext.teletext_backend.domain.services;

import java.text.Normalizer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.domain.models.City;
import pl.studia.teletext.teletext_backend.domain.repositories.CityRepository;
import pl.studia.teletext.teletext_backend.exceptions.CityNotFoundException;

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
      .filter(c -> {
        var normalizedName = normalizeCityName(c.name());
        log.debug("Comparing with city: {} (normalized: {}, normalized input: {})", c.name(), normalizedName, normalizedInputName);
        return normalizedInputName.equals(normalizedName);
      })
      .findFirst()
      .orElseThrow(() -> new CityNotFoundException("City with name " + name + " not found"));
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
