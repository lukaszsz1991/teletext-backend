package pl.studia.teletext.teletext_backend.integration.lotto.domain;

import java.time.LocalDateTime;

public record LottoInfoResponse(
    String gameType,
    LocalDateTime nextDrawDate,
    Double closestPrizeValue,
    String draws,
    String couponPrice) {}
