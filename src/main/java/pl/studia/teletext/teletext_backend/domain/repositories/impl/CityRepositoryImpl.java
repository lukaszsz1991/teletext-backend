package pl.studia.teletext.teletext_backend.domain.repositories.impl;

import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.studia.teletext.teletext_backend.domain.models.City;
import pl.studia.teletext.teletext_backend.domain.repositories.CityRepository;

@Repository
@RequiredArgsConstructor
public class CityRepositoryImpl implements CityRepository {

  private static final String CITIES_FILE_PATH = "/data/cities.csv";

  private final Validator validator;

  @Override
  public List<City> getAllCities() {
    return readCsvFile().stream().map(this::mapToCity).toList();
  }

  public void validateCity(City city) {
    var violations = validator.validate(city);
    if (!violations.isEmpty()) {
      var firstMsg = violations.stream().findFirst().get().getMessage();
      throw new ValidationException("City validation failed: " + firstMsg + "; City: " + city);
    }
  }

  private List<String> readCsvFile() {
    return Optional.ofNullable(getClass().getResourceAsStream(CITIES_FILE_PATH))
        .map(
            is -> {
              try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().toList();
              } catch (IOException ex) {
                throw new RuntimeException("Error reading cities file", ex);
              }
            })
        .orElseThrow(() -> new RuntimeException("Cities file not found: " + CITIES_FILE_PATH));
  }

  private City mapToCity(String csvLine) {
    String[] parts = csvLine.split(",", 3);
    String name = parts[0];
    double latitude = parseCoordinate(parts[1]);
    double longitude = parseCoordinate(parts[2]);
    var city = new City(name, latitude, longitude);
    validateCity(city);
    return city;
  }

  private double parseCoordinate(String value) {
    if (value.isBlank()) throw new ValidationException("City coordinate value is blank");
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException ex) {
      throw new ValidationException("Invalid city coordinate value: " + value, ex);
    }
  }
}
