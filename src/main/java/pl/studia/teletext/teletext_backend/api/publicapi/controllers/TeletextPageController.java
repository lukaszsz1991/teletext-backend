package pl.studia.teletext.teletext_backend.api.publicapi.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageStatsService;

@RestController
@RequestMapping("/api/public/pages")
@RequiredArgsConstructor
public class TeletextPageController {

  private final TeletextPageService teletextPageService;
  private final TeletextPageStatsService teletextPageStatsService;

  @GetMapping
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam TeletextCategory category, @RequestParam(required = false) String title) {
    return ResponseEntity.ok(teletextPageService.getPagesByCategory(category, title));
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<TeletextDetailedPageResponse> getPageByNumber(
      @PathVariable Integer pageNumber) {
    var page = teletextPageService.getPageWithContentByPageNumber(pageNumber);
    teletextPageStatsService.recordPageVisit(page.id()); // TODO: move it to the middleware
    return ResponseEntity.ok(page);
  }
}
