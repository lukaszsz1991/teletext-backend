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
    //TODO(swagger): change '?' to correct DTO
    //TODO: change to TeletextSimplePageResponse (without content)
    //TODO: add pagination and filtering (filter by: pagenumber [between?], title and category)
    //TODO: sort by page number asc (or add sorting as an option)
    return ResponseEntity.ok(teletextPageService.getAllPagesWithContent());
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<?> getPageByNumber(
    @PathVariable Integer pageNumber
  ) {
    var page = teletextPageService.getPageWithContent(pageNumber);
    teletextPageStatsService.recordPageVisit(page.id()); // TODO: move it to the middleware
    return ResponseEntity.ok(page);
  }
}
