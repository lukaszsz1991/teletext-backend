package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import java.time.LocalDateTime;

public record LotteryResponse(
  String gameType,
  LocalDateTime nextDrawDate,
  Double nextDrawPrize,
  String draws,
  String couponPrice,
  LocalDateTime lastDrawDate,
  int[] lastDrawResults
) {}
