package pl.studia.teletext.teletext_backend.api.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.PageCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.PageUpdateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextAdminPageResponse;
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
  @Operation(
      summary = "Get all teletext pages",
      description =
          "Returns a list of teletext pages. Optionally filter by category and include inactive pages.")
  public ResponseEntity<List<TeletextPageResponse>> getAllPages(
      @RequestParam(required = false) TeletextCategory category,
      @RequestParam(defaultValue = "false") boolean includeInactive) {
    var results = pageService.getAllPages(category, includeInactive);
    return ResponseEntity.ok(results);
  }

  @GetMapping("{id}")
  @Operation(
      summary = "Get teletext page by ID",
      description =
          "Returns a single teletext page by its ID, including its content. Shows even inactive pages.")
  public ResponseEntity<TeletextAdminPageResponse> getPageById(@PathVariable long id) {
    var result = pageService.getPageWithContentById(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  @Operation(
      summary = "Create a new teletext page",
      description =
          "Creates a new teletext page based on the provided data. Must declare type of the page (MANUAL or TEMPLATE).")
  public ResponseEntity<Void> createPage(@RequestBody @Valid PageCreateRequest request) {
    var result = pageService.createPage(request);
    var uri = URI.create("/api/admin/pages/" + result.id());
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  @Operation(
      summary = "Update an existing teletext page",
      description =
          "Updates the details of an existing teletext page identified by its ID. Must declare type of the page (MANUAL or TEMPLATE).")
  public ResponseEntity<TeletextAdminPageResponse> updatePage(
      @PathVariable Long id, @RequestBody @Valid PageUpdateRequest request) {
    var result = pageService.updatePage(id, request);
    return ResponseEntity.ok(result);
  }

  @PatchMapping("{id}/activate")
  @Operation(
      summary = "Activate a teletext page",
      description =
          "Activates a teletext page identified by its ID. Can activate only previously deactivated pages.")
  public ResponseEntity<Void> activatePage(@PathVariable Long id) {
    pageService.activatePage(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  @Operation(
      summary = "Deactivate a teletext page",
      description =
          "Deactivates a teletext page identified by its ID. The page will no longer be visible in public API.")
  public ResponseEntity<Void> deletePage(@PathVariable Long id) {
    pageService.deactivatePage(id);
    return ResponseEntity.noContent().build();
  }
}
