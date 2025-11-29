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
import pl.studia.teletext.teletext_backend.domain.services.TeletextCategoryService;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageService;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageStatsService;

@RestController
@RequestMapping("/api/public/pages")
@RequiredArgsConstructor
public class TeletextPageController {

  private final TeletextPageService teletextPageService;
  private final TeletextPageStatsService teletextPageStatsService;
  private final TeletextCategoryService teletextCategoryService;

  @GetMapping
  public ResponseEntity<List<TeletextPageResponse>> getAllPagesByCategory(
    @RequestParam TeletextCategory category
  ) {
    //TODO: add pagination and filtering (filter by: pagenumber [between?], title and category)
    //TODO: sort by page number asc (or add sorting as an option)
    return ResponseEntity.ok(teletextPageService.getPagesByCategory(category));
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<TeletextDetailedPageResponse> getPageByNumber(
    @PathVariable Integer pageNumber
  ) {
    var page = teletextPageService.getPageWithContent(pageNumber);
    teletextPageStatsService.recordPageVisit(page.id()); // TODO: move it to the middleware
    return ResponseEntity.ok(page);
  }
}
