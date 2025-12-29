package pl.studia.teletext.teletext_backend.teletext.category.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.teletext.category.api.dto.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.category.service.TeletextCategoryService;

@RestController
@RequestMapping("/api/public/categories")
@RequiredArgsConstructor
@Tag(
    name = "Teletext Categories",
    description = "Endpoints for retrieving teletext categories"
)
public class TeletextCategoryController {

  private final TeletextCategoryService teletextCategoryService;

  @GetMapping
  @Operation(
      summary = "Get All Categories",
      description = "Retrieve a list of all teletext categories"
  )
  public ResponseEntity<TeletextCategoryResponse[]> getCategories() {
    return ResponseEntity.ok(teletextCategoryService.getAllCategories());
  }

  @GetMapping("{category}")
  @Operation(
      summary = "Get Category By Name",
      description = "Retrieve a specific teletext category by its name"
  )
  public ResponseEntity<TeletextCategoryResponse> getCategoryByName(
      @PathVariable TeletextCategory category) {
    return ResponseEntity.ok(teletextCategoryService.getCategoryByName(category));
  }
}
