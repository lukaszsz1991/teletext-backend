package pl.studia.teletext.teletext_backend.api.publicapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageService;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageStatsService;

@RestController
@RequestMapping("/api/public/pages")
@RequiredArgsConstructor
public class TeletextPageController {

  private final TeletextPageService teletextPageService;
  private final TeletextPageStatsService teletextPageStatsService;

  @GetMapping
  public ResponseEntity<?> getAllPages() {
    return ResponseEntity.ok(teletextPageService.getAllPagesWithContent());
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<?> getPageByNumber(
    @PathVariable Integer pageNumber
  ) {
    var page = teletextPageService.getPageWithContent(pageNumber);
    teletextPageStatsService.recordPageVisit(page.id());
    return ResponseEntity.ok(page);
  }
}
