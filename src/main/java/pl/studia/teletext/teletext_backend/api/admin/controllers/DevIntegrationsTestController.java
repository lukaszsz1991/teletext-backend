package pl.studia.teletext.teletext_backend.api.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.CurrencyExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.LottoExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.NewsExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.WeatherExternalDataMapper;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.domain.services.integrations.CurrencyService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.LotteryService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.NewsService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.WeatherService;

@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
@Profile({"dev", "local-dev"})
public class DevIntegrationsTestController {

  private final LottoExternalDataMapper lottoExternalDataMapper;
  private final WeatherExternalDataMapper weatherExternalDataMapper;
  private final CurrencyExternalDataMapper currencyExternalDataMapper;
  private final NewsExternalDataMapper newsExternalDataMapper;
  private final CurrencyService currencyService;
  private final WeatherService weatherService;
  private final LotteryService lotteryService;
  private final NewsService newsService;

  @GetMapping("/currencies")
  public ResponseEntity<?> getCurrencies(
    @RequestParam(defaultValue = "usd,eur") String[] codes,
    @RequestParam(defaultValue = "5") int lastCount
  ) {
    var result = currencyService.getLastCurrencyRates(codes, lastCount)
      .map(currencyExternalDataMapper::toExternalDataResponse)
      .collectList()
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/weather/{city}")
  public ResponseEntity<?> getWeatherByCity(
    @PathVariable String city
  ) {
    var result = weatherService.getWeeklyWeatherForCity(city)
      .map(weatherExternalDataMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/lotto")
  public ResponseEntity<?> getLottoData() {
    var result = lotteryService.getLottoResponse()
      .map(lottoExternalDataMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/news")
  public ResponseEntity<?> getNewsExternalData(
    @RequestParam String category,
    @RequestParam(defaultValue = "true") boolean isPolish
    ) {
    var result = newsService.getLatestNews(isPolish, NewsCategory.valueOf(category.toUpperCase()))
      .map(newsExternalDataMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }
}
