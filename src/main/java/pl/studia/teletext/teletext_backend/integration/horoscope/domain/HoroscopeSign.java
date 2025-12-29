package pl.studia.teletext.teletext_backend.integration.horoscope.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HoroscopeSign {
  BARAN,
  BYK,
  BLIZNIETA,
  RAK,
  LEW,
  PANNA,
  WAGA,
  SKORPION,
  STRZELEC,
  KOZIOROZEC,
  WODNIK,
  RYBY;

  @JsonCreator
  public static HoroscopeSign fromString(String value) {
    return HoroscopeSign.valueOf(value.trim().toUpperCase());
  }
}
