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
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.FootballExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.HoroscopeExternalMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.JobsExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.LottoExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.NewsExternalDataMapper;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.WeatherExternalDataMapper;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleRequest;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.domain.services.integrations.CurrencyService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.FootballService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.HoroscopeService;
import pl.studia.teletext.teletext_backend.domain.services.integrations.JobsService;
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
  private final JobsExternalDataMapper jobsExternalDataMapper;
  private final HoroscopeExternalMapper horoscopeExternalMapper;
  private final FootballExternalDataMapper footballExternalMapper;
  private final CurrencyService currencyService;
  private final WeatherService weatherService;
  private final LotteryService lotteryService;
  private final NewsService newsService;
  private final JobsService jobsService;
  private final HoroscopeService horoscopeService;
  private final FootballService footballService;

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

  @GetMapping("/jobs/{addedOrder}")
  public ResponseEntity<?> getJobByAddingOrder(
    @PathVariable int addedOrder,
    @RequestParam String keyword,
    @RequestParam String location
  ){
    var body = new JoobleRequest(keyword, location);
    var result = jobsService.getJobByAddingOrder(body, addedOrder)
      .map(jobsExternalDataMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/horoscope/{sign}")
  public ResponseEntity<?> getJobByAddingOrder(
    @PathVariable HoroscopeSign sign,
    @RequestParam(defaultValue = "false") boolean forTomorrow
  ){
    var result = horoscopeService.getSingleSignHoroscope(sign, forTomorrow)
      .map(horoscopeExternalMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/football/{league}/table")
  public ResponseEntity<?> getLeagueTable(
    @PathVariable FootballLeague league
  ){
    var result = footballService.getTableForLeague(league)
      .map(footballExternalMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/football/{league}/matches/{week}")
  public ResponseEntity<?> getMatchesForWeek(
    @PathVariable FootballLeague league,
    @PathVariable Integer week
  ){
    var result = footballService.getMatchesForLeague(league, week)
      .map(footballExternalMapper::toExternalDataResponse)
      .block();
    return ResponseEntity.ok(result);
  }
}
