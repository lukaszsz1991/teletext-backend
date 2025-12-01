package pl.studia.teletext.teletext_backend.api.admin.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
