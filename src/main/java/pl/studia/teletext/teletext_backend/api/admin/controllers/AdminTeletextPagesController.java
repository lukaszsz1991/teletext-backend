package pl.studia.teletext.teletext_backend.api.admin.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.PageCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.PageUpdateRequest;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

@RestController
@RequestMapping("/api/admin/pages")
@RequiredArgsConstructor
@Tag(
    name = "Admin Teletext Pages",
    description = "Endpoints for managing teletext pages in the admin panel")
public class AdminTeletextPagesController {

  private final TeletextPageService pageService;

  @GetMapping
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam(required = false) TeletextCategory category,
      @RequestParam(defaultValue = "false") boolean includeInactive) {
    var results = pageService.getAllPages(category, includeInactive);
    return ResponseEntity.ok(results);
  }

  @GetMapping("{id}")
  public ResponseEntity<TeletextDetailedPageResponse> getPageById(@PathVariable long id) {
    var result = pageService.getPageWithContentById(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<TeletextDetailedPageResponse> createPage(
      @RequestBody @Valid PageCreateRequest request) {
    var result = pageService.createPage(request);
    var uri = URI.create("/api/admin/pages/" + result.id());
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<TeletextDetailedPageResponse> updatePage(
      @PathVariable Long id, @RequestBody @Valid PageUpdateRequest request) {
    var result = pageService.updatePage(id, request);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("{id}/activate")
  public ResponseEntity<Void> activatePage(@PathVariable Long id) {
    // TODO: implement
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deletePage(@PathVariable Long id) {
    // TODO: implement
    return ResponseEntity.noContent().build();
  }
}
