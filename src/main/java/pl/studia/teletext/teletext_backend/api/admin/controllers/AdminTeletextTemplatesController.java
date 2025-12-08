package pl.studia.teletext.teletext_backend.api.admin.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateResponse;
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
      @RequestParam(required = false) TeletextCategory category) {
    var results =
        pageTemplateService.getAllTemplates(category).stream()
            .map(pageTemplateMapper::toResponse)
            .toList();
    return ResponseEntity.ok(results);
  }


  @GetMapping("/{id}")
  public ResponseEntity<TeletextPageTemplateResponse> getTemplate(
    @PathVariable Long id
  ) {
    //TODO: implement
    // should return more complete response with all template details and linked page
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<TeletextPageTemplateResponse> createTemplate() {
    //TODO: implement
    return ResponseEntity.status(201).build();
  }

  @PutMapping
  public ResponseEntity<TeletextPageTemplateResponse> updateTemplate() {
    //TODO: implement
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<Void> activateTemplate(
    @PathVariable Long id
  ) {
    //TODO: implement
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTemplate(
    @PathVariable Long id
  ) {
    //TODO: implement
    return ResponseEntity.noContent().build();
  }
}
