package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

@RestController
@RequestMapping("/api/public/pages")
@RequiredArgsConstructor
public class TeletextPageController {

  private final TeletextPageService teletextPageService;

  @GetMapping
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam TeletextCategory category, @RequestParam(required = false) String title) {
    return ResponseEntity.ok(teletextPageService.getPagesByCategory(category, title));
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<TeletextDetailedPageResponse> getPageByNumber(
      @PathVariable int pageNumber) {
    var page = teletextPageService.viewPageByPageNumber(pageNumber);
    return ResponseEntity.ok(page);
  }
}
