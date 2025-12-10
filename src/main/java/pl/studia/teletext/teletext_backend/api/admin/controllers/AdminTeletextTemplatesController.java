package pl.studia.teletext.teletext_backend.api.admin.controllers;

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
public class AdminTeletextTemplatesController {

  private final TeletextPageTemplateService pageTemplateService;
  private final TeletextPageTemplateMapper pageTemplateMapper;

  @GetMapping
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
  public ResponseEntity<TeletextPageFullTemplateResponse> getTemplate(@PathVariable Long id) {
    var result = pageTemplateService.getTemplateById(id);
    var response = pageTemplateMapper.toFullResponse(result);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<TeletextPageTemplateResponse> createTemplate(
      @RequestBody @Valid TeletextPageTemplateCreateRequest request) {
    var template = pageTemplateService.createTemplate(request);
    var response = pageTemplateMapper.toResponse(template);
    var uri = URI.create("/api/admin/templates/" + response.id());
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("{id}")
  public ResponseEntity<TeletextPageTemplateResponse> updateTemplate(
      @PathVariable Long id, @RequestBody @Valid TeletextPageTemplateUpdateRequest request) {
    var updatedTemplate = pageTemplateService.updateTemplate(id, request);
    var response = pageTemplateMapper.toResponse(updatedTemplate);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<Void> activateTemplate(@PathVariable Long id) {
    pageTemplateService.activateTemplate(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTemplate(@PathVariable Long id) {
    // mark it in the docs as soft-delete - can be reactivated by PATCH:
    // /api/admin/templates/{id}/activate
    pageTemplateService.deactivateTemplate(id);
    return ResponseEntity.noContent().build();
  }
}
