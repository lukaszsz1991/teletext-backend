package pl.studia.teletext.teletext_backend.teletext.page.api.publicapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Teletext Pages", description = "Endpoints for retrieving teletext pages")
public class TeletextPageController {

  private final TeletextPageService teletextPageService;

  @GetMapping
  @Operation(
      summary = "Get all teletext pages by category",
      description = "Retrieve a list of teletext pages filtered by category and optional title")
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam TeletextCategory category, @RequestParam(required = false) String title) {
    return ResponseEntity.ok(teletextPageService.getPagesByCategory(category, title));
  }

  @GetMapping("{pageNumber}")
  @Operation(
      summary = "Get teletext page by page number",
      description = "Retrieve detailed information about a teletext page by its page number")
  public ResponseEntity<TeletextDetailedPageResponse> getPageByNumber(
      @PathVariable int pageNumber) {
    var page = teletextPageService.viewPageByPageNumber(pageNumber);
    return ResponseEntity.ok(page);
  }
}
