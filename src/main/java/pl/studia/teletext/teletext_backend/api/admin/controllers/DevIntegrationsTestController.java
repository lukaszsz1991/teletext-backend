package pl.studia.teletext.teletext_backend.api.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.domain.services.integrations.CurrencyService;

@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
@Profile({"dev", "local-dev"})
public class DevIntegrationsTestController {

  private final CurrencyService currencyService;

  @GetMapping("/currencies")
  public ResponseEntity<?> testEndpoint(
    @RequestParam(defaultValue = "usd,eur") String[] codes,
    @RequestParam(defaultValue = "5") int lastCount
  ) {
    var result = currencyService.getLastCurrencyRates(codes, lastCount)
      .collectList()
      .block();
    return ResponseEntity.ok(result);
  }
}
