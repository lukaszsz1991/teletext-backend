package pl.studia.teletext.teletext_backend.integration.news.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record NewsDataResponse(String status, int totalResults, NewsArticle[] results) {
  public record NewsArticle(
      String link,
      String title,
      String description,
      String[] keywords,
      String[] creator,
      String[] country,
      String[] category,
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime pubDate,
      String sourceName,
      String sourceUrl) {}
}
