package pl.studia.teletext.teletext_backend.teletext.template.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.common.dto.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.common.utils.time.FlexibleDateParser;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballLeague;
import pl.studia.teletext.teletext_backend.integration.highlightly.mapper.FootballExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.highlightly.service.FootballService;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeSign;
import pl.studia.teletext.teletext_backend.integration.horoscope.mapper.HoroscopeExternalMapper;
import pl.studia.teletext.teletext_backend.integration.horoscope.service.HoroscopeService;
import pl.studia.teletext.teletext_backend.integration.jooble.domain.JoobleRequest;
import pl.studia.teletext.teletext_backend.integration.jooble.mapper.JobsExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.jooble.service.JobsService;
import pl.studia.teletext.teletext_backend.integration.lotto.mapper.LottoExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.lotto.service.LotteryService;
import pl.studia.teletext.teletext_backend.integration.nbp.mapper.CurrencyExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.nbp.service.CurrencyService;
import pl.studia.teletext.teletext_backend.integration.news.domain.NewsCategory;
import pl.studia.teletext.teletext_backend.integration.news.mapper.NewsExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.news.service.NewsService;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpChannel;
import pl.studia.teletext.teletext_backend.integration.tvp.mapper.TvProgramExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.tvp.service.TvProgramService;
import pl.studia.teletext.teletext_backend.integration.weather.mapper.WeatherExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.weather.service.WeatherService;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPage;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPageContent;
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
  private final TvProgramService tvProgramService;
  private final CurrencyExternalDataMapper currencyExternalDataMapper;
  private final FootballExternalDataMapper footballExternalDataMapper;
  private final NewsExternalDataMapper newsExternalDataMapper;
  private final LottoExternalDataMapper lottoExternalDataMapper;
  private final JobsExternalDataMapper jobsExternalDataMapper;
  private final WeatherExternalDataMapper weatherExternalDataMapper;
  private final HoroscopeExternalMapper horoscopeExternalMapper;
  private final TvProgramExternalDataMapper tvProgramExternalDataMapper;

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
          case TV_PROGRAM -> getTvProgramData(config);
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
    return currencyService
        .getLastCurrencyRates(currencyCode, lastCount)
        .map(currencyExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getFootballMatchData(Map<String, Object> config) {
    var league = FootballLeague.fromString(((String) config.get("league")));
    var week = (int) config.get("week");

    return footballService
        .getMatchesForLeague(league, week)
        .map(footballExternalDataMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getFootballTableData(Map<String, Object> config) {
    var league = FootballLeague.fromString(((String) config.get("league")));

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
    var keywords = (String) config.get("keywords");
    var location = (String) config.get("location");
    var jobNo = (int) config.getOrDefault("addedOrder", 1);

    var request = new JoobleRequest(keywords, location);
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
    var sign = HoroscopeSign.fromString(((String) config.get("sign")));
    var forTomorrow = (boolean) config.getOrDefault("forTomorrow", false);
    return horoscopeService
        .getSingleSignHoroscope(sign, forTomorrow)
        .map(horoscopeExternalMapper::toExternalDataResponse);
  }

  private Mono<ExternalDataResponse> getTvProgramData(Map<String, Object> config) {
    var channel = TvpChannel.fromString((String) config.get("channelName"));
    var date = FlexibleDateParser.parse((String) config.get("date")).toLocalDate();
    return tvProgramService
        .getTvProgram(channel, date)
        .map(tvProgramExternalDataMapper::toExternalDataResponse);
  }
}
