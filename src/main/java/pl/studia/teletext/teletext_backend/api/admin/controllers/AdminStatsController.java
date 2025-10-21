package pl.studia.teletext.teletext_backend.api.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageService;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageStatsService;

@Controller
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

  private final TeletextPageStatsService teletextPageStatsService;

  @GetMapping("pages/{pageNumber}")
  public ResponseEntity<?> getPageStats(
    @PathVariable Integer pageNumber
  ) {
    var page = teletextPageStatsService.getStatsForPage(pageNumber);
    return ResponseEntity.ok(page);
  }
}
