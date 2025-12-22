package pl.studia.teletext.teletext_backend.api.admin.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.*;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleRequest;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpChannel;
import pl.studia.teletext.teletext_backend.domain.services.integrations.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
@Profile({"dev", "local-dev"})
@Tag(
    name = "Development Integrations Test Controller - available only in dev mode",
    description =
        "Endpoints for testing external integrations during development. Not available in production.")
public class DevIntegrationsTestController {

  private final LottoExternalDataMapper lottoExternalDataMapper;
  private final WeatherExternalDataMapper weatherExternalDataMapper;
  private final CurrencyExternalDataMapper currencyExternalDataMapper;
  private final NewsExternalDataMapper newsExternalDataMapper;
  private final JobsExternalDataMapper jobsExternalDataMapper;
  private final HoroscopeExternalMapper horoscopeExternalMapper;
  private final FootballExternalDataMapper footballExternalMapper;
  private final TvProgramExternalDataMapper tvProgramExternalDataMapper;
  private final CurrencyService currencyService;
  private final WeatherService weatherService;
  private final LotteryService lotteryService;
  private final NewsService newsService;
  private final JobsService jobsService;
  private final HoroscopeService horoscopeService;
  private final FootballService footballService;
  private final TvProgramService tvProgramService;

  @GetMapping("/currencies")
  public ResponseEntity<?> getCurrencies(
      @RequestParam(defaultValue = "usd") String code,
      @RequestParam(defaultValue = "5") int lastCount) {
    var result =
        currencyService
            .getLastCurrencyRates(code, lastCount)
            .map(currencyExternalDataMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/weather/{city}")
  public ResponseEntity<?> getWeatherByCity(@PathVariable String city) {
    var result =
        weatherService
            .getWeeklyWeatherForCity(city)
            .map(weatherExternalDataMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/lotto")
  public ResponseEntity<?> getLottoData() {
    var result =
        lotteryService
            .getLottoResponse()
            .map(lottoExternalDataMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/news")
  public ResponseEntity<?> getNewsExternalData(
      @RequestParam String category, @RequestParam(defaultValue = "true") boolean isPolish) {
    var result =
        newsService
            .getLatestNews(isPolish, NewsCategory.valueOf(category.toUpperCase()))
            .map(newsExternalDataMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/jobs/{addedOrder}")
  public ResponseEntity<?> getJobByAddingOrder(
      @PathVariable int addedOrder, @RequestParam String keyword, @RequestParam String location) {
    var body = new JoobleRequest(keyword, location);
    var result =
        jobsService
            .getJobByAddingOrder(body, addedOrder)
            .map(jobsExternalDataMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/horoscope/{sign}")
  public ResponseEntity<?> getJobByAddingOrder(
      @PathVariable HoroscopeSign sign, @RequestParam(defaultValue = "false") boolean forTomorrow) {
    var result =
        horoscopeService
            .getSingleSignHoroscope(sign, forTomorrow)
            .map(horoscopeExternalMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/football/{league}/table")
  public ResponseEntity<?> getLeagueTable(@PathVariable FootballLeague league) {
    var result =
        footballService
            .getTableForLeague(league)
            .map(footballExternalMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/football/{league}/matches/{week}")
  public ResponseEntity<?> getMatchesForWeek(
      @PathVariable FootballLeague league, @PathVariable Integer week) {
    var result =
        footballService
            .getMatchesForLeague(league, week)
            .map(footballExternalMapper::toExternalDataResponse)
            .block();
    return ResponseEntity.ok(result);
  }

  @GetMapping("tv-program")
  public ResponseEntity<?> getMatchesForWeek(
    @RequestParam TvpChannel channelName, @RequestParam LocalDate date) {
    var result =
      tvProgramService
        .getTvProgram(channelName, date)
        .map(tvProgramExternalDataMapper::toExternalDataResponse)
        .block();
    return ResponseEntity.ok(result);
  }
}
