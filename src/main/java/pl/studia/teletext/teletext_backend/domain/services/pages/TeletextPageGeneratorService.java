package pl.studia.teletext.teletext_backend.domain.services.pages;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals.*;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleRequest;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageContent;
import pl.studia.teletext.teletext_backend.domain.services.integrations.*;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TeletextPageGeneratorService {

  private final CurrencyService currencyService;
  private final FootballService footballService;
  private final NewsService newsService;
  private final LotteryService lotteryService;
  private final JobsService jobsService;
  private final WeatherService weatherService;
  private final HoroscopeService horoscopeService;
  private final CurrencyExternalDataMapper currencyExternalDataMapper;
  private final FootballExternalDataMapper footballExternalDataMapper;
  private final NewsExternalDataMapper newsExternalDataMapper;
  private final LottoExternalDataMapper lottoExternalDataMapper;
  private final JobsExternalDataMapper jobsExternalDataMapper;
  private final WeatherExternalDataMapper weatherExternalDataMapper;
  private final HoroscopeExternalMapper horoscopeExternalMapper;

  public Mono<TeletextPage> generatePageFromTemplate(TeletextPage page) {
    var template =
        Optional.ofNullable(page.getTemplate())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Do generatora stron z szablonu przekazano stronę bez template_id. Strona: "
                            + page.getPageNumber()));

    if (template.getSource() == null) {
      return Mono.error(
          new IllegalArgumentException("Brak źródła w szablonie strony: " + template.getName()));
    }

    var config = template.getConfigJson();
    Mono<ExternalDataResponse> responseMono =
        switch (template.getSource()) {
          case EXCHANGE_RATE -> getNbpData(config);
          case SPORT_MATCHES -> getFootballMatchData(config);
          case SPORT_TABLE -> getFootballTableData(config);
          case NEWS -> getNewsData(config);
          case LOTTERY -> getLottoData();
          case JOB_OFFERS -> getJobOffersData(config);
          case WEATHER -> getWeatherData(config);
          case HOROSCOPE -> getHoroscopeData(config);
          default ->
              Mono.error(
                  new IllegalArgumentException(
                      "Nieznane źródło ("
                          + template.getSource()
                          + ") w szablonie strony: "
                          + template.getName()));
        };

    return responseMono.map(
        data -> {
          var content = new TeletextPageContent();
          content.setTitle(data.title());
          content.setDescription(data.description());
          content.setAdditionalData(data.additionalData());
          content.setSource(template.getSource());
          content.setCreatedAt(Timestamp.from(Instant.now()));
          content.setUpdatedAt(Timestamp.from(Instant.now()));
          page.setContent(content);
          return page;
        });
  }

  private Mono<ExternalDataResponse> getNbpData(Map<String, Object> config) {
    int defaultLastCount = 5;
    var currencyCode = (String) config.get("currencyCode");
    var lastCount = (int) config.getOrDefault("lastCount", defaultLastCount);
    var currency =
        Optional.ofNullable(currencyCode)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "W konfiguracji szablonu NBP brakuje pola 'currencyCode'"));
    return currencyService
        .getLastCurrencyRates(currency, lastCount)
        .map(currencyExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getFootballMatchData(Map<String, Object> config) {
    var league = FootballLeague.valueOf(((String) config.get("league")).toUpperCase());
    var week = (int) config.get("week");

    return footballService
        .getMatchesForLeague(league, week)
        .map(footballExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getFootballTableData(Map<String, Object> config) {
    var league = FootballLeague.valueOf(((String) config.get("league")).toUpperCase());

    return footballService
        .getTableForLeague(league)
        .map(footballExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getNewsData(Map<String, Object> config) {
    var isPolish = (boolean) config.getOrDefault("isPolish", true);
    var category = NewsCategory.valueOf(((String) config.get("category")).toUpperCase());

    return newsService
        .getLatestNews(isPolish, category)
        .map(newsExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getLottoData() {
    return lotteryService.getLottoResponse().map(lottoExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getJobOffersData(Map<String, Object> config) {
    var request = (JoobleRequest) config.get("joobleRequest");
    var jobNo = (int) config.get("addedOrder");

    return jobsService
        .getJobByAddingOrder(request, jobNo)
        .map(jobsExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getWeatherData(Map<String, Object> config) {
    var city = (String) config.get("city");
    return weatherService
        .getWeeklyWeatherForCity(city)
        .map(weatherExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getHoroscopeData(Map<String, Object> config) {
    var sign = HoroscopeSign.valueOf(((String) config.get("sign")).toUpperCase());
    var forTomorrow = (boolean) config.getOrDefault("forTomorrow", false);
    return horoscopeService
        .getSingleSignHoroscope(sign, forTomorrow)
        .map(horoscopeExternalMapper::toExternalDataResponse);
  }
}
