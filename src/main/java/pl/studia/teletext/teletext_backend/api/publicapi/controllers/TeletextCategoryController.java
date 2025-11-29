package pl.studia.teletext.teletext_backend.api.publicapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.TeletextCategoryService;

@RestController
@RequestMapping("/api/public/categories")
@RequiredArgsConstructor
public class TeletextCategoryController {

  private final TeletextCategoryService teletextCategoryService;

  @GetMapping
  public ResponseEntity<TeletextCategoryResponse[]> getCategories() {
    return ResponseEntity.ok(teletextCategoryService.getAllCategories());
  }

  @GetMapping("{category}")
  public ResponseEntity<TeletextCategoryResponse> getCategoryByName(
    @PathVariable TeletextCategory category
  ) {
    return ResponseEntity.ok(teletextCategoryService.getCategoryByName(category));
  }
}
