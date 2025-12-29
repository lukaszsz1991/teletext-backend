package pl.studia.teletext.teletext_backend.integration.lotto.domain;

import java.time.LocalDateTime;
import java.util.List;

public record LottoResultResponse(
    LocalDateTime drawDate, String gameType, List<ResultItem> results) {
  public record ResultItem(int[] resultsJson) {}
}
