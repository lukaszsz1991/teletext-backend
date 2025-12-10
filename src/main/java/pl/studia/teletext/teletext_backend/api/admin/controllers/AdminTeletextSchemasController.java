package pl.studia.teletext.teletext_backend.api.admin.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.admin.dtos.ConfigSchemaResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextSchemaService;

@RestController
@RequestMapping("/api/admin/schemas")
@RequiredArgsConstructor
public class AdminTeletextSchemasController {

  private final TeletextSchemaService teletextSourceService;

  @GetMapping("{source}")
  public ResponseEntity<ConfigSchemaResponse> getSourceSchema(@PathVariable TeletextSource source) {
    var result = teletextSourceService.getSchema(source);
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<List<ConfigSchemaResponse>> getAllSchemas() {
    var result = teletextSourceService.getAllSchemas();
    return ResponseEntity.ok(result);
  }
}
