package pl.studia.teletext.teletext_backend.api.admin.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

@RestController
@RequestMapping("/api/admin/pages")
@RequiredArgsConstructor
public class AdminTeletextPagesController {

  private final TeletextPageService pageService;

  @GetMapping
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam(required = false) TeletextCategory category,
      @RequestParam(defaultValue = "false") boolean includeInactive) {
    var results = pageService.getAllPages(category, includeInactive);
    return ResponseEntity.ok(results);
  }
}
