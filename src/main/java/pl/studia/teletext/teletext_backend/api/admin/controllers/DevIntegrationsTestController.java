package pl.studia.teletext.teletext_backend.api.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.domain.services.integrations.CurrencyService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.WeatherService;

@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
@Profile({"dev", "local-dev"})
public class DevIntegrationsTestController {

  private final CurrencyService currencyService;
  private final WeatherService weatherService;

  @GetMapping("/currencies")
  public ResponseEntity<?> getCurrencies(
    @RequestParam(defaultValue = "usd,eur") String[] codes,
    @RequestParam(defaultValue = "5") int lastCount
  ) {
    var result = currencyService.getLastCurrencyRates(codes, lastCount)
      .collectList()
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/weather")
  public ResponseEntity<?> getWeather() {
    var result = weatherService.getWeeklyWeatherForAllCities()
      .collectList()
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/weather/{city}")
  public ResponseEntity<?> getWeatherByCity(
    @PathVariable String city
  ) {
    var result = weatherService.getWeeklyWeatherForCity(city)
      .block();
    return ResponseEntity.ok(result);
  }
}
