package pl.studia.teletext.teletext_backend.api.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageFullTemplateResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateUpdateRequest;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextPageTemplateMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageTemplateService;

@RestController
@RequestMapping("/api/admin/templates")
@RequiredArgsConstructor
@Tag(
    name = "Teletext Page Templates Management Controller",
    description = "Operations for teletext page templates. Allowed only for ADMIN role.")
public class AdminTeletextTemplatesController {

  private final TeletextPageTemplateService pageTemplateService;
  private final TeletextPageTemplateMapper pageTemplateMapper;

  @GetMapping
  @Operation(
      summary = "Get all teletext page templates",
      description =
          "Returns a list of teletext page templates. Optionally filter by category and include inactive templates.")
  @ApiResponse(responseCode = "200", description = "Templates retrieved successfully.")
  public ResponseEntity<List<TeletextPageTemplateResponse>> getTemplates(
      @RequestParam(required = false) TeletextCategory category,
      @RequestParam(defaultValue = "false") boolean includeInactive) {
    var results =
        pageTemplateService.getAllTemplates(category, includeInactive).stream()
            .map(pageTemplateMapper::toResponse)
            .toList();
    return ResponseEntity.ok(results);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get teletext page template by ID",
      description =
          "Returns a single teletext page template by its ID. Shows only active templates.")
  @ApiResponse(responseCode = "200", description = "Template retrieved successfully.")
  public ResponseEntity<TeletextPageFullTemplateResponse> getTemplate(@PathVariable Long id) {
    var result = pageTemplateService.getTemplateById(id);
    var response = pageTemplateMapper.toFullResponse(result);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  @Operation(
      summary = "Create teletext page template",
      description =
          "Creates a new teletext page template and returns the created template with proper location in the header.")
  @ApiResponse(responseCode = "201", description = "Template successfully created.")
  public ResponseEntity<TeletextPageTemplateResponse> createTemplate(
      @RequestBody @Valid TeletextPageTemplateCreateRequest request) {
    var template = pageTemplateService.createTemplate(request);
    var response = pageTemplateMapper.toResponse(template);
    var uri = URI.create("/api/admin/templates/" + response.id());
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("{id}")
  @Operation(
      summary = "Update teletext page template",
      description = "Updates an existing teletext page template by its ID.")
  @ApiResponse(responseCode = "200", description = "Template successfully updated.")
  public ResponseEntity<TeletextPageTemplateResponse> updateTemplate(
      @PathVariable Long id, @RequestBody @Valid TeletextPageTemplateUpdateRequest request) {
    var updatedTemplate = pageTemplateService.updateTemplate(id, request);
    var response = pageTemplateMapper.toResponse(updatedTemplate);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}/activate")
  @Operation(
      summary = "Activate teletext page template",
      description = "Reactivates a previously soft-deleted teletext page template by its ID.")
  @ApiResponse(responseCode = "204", description = "Template successfully activated.")
  public ResponseEntity<Void> activateTemplate(@PathVariable Long id) {
    pageTemplateService.activateTemplate(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Deletes teletext page template",
      description = "Marks a teletext page template as inactive (soft-delete) by its ID.")
  @ApiResponse(responseCode = "204", description = "Template successfully soft-deleted.")
  public ResponseEntity<?> deleteTemplate(@PathVariable Long id) {
    pageTemplateService.deactivateTemplate(id);
    return ResponseEntity.noContent().build();
  }
}
