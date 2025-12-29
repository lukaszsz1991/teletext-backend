package pl.studia.teletext.teletext_backend.teletext.schema.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.ConfigSchemaResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;
import pl.studia.teletext.teletext_backend.teletext.schema.service.TeletextSchemaService;

@RestController
@RequestMapping("/api/admin/schemas")
@RequiredArgsConstructor
@Tag(
    name = "Teletext Schemas Management Controller",
    description = "Operations for teletext configuration schemas.")
public class AdminTeletextSchemasController {

  private final TeletextSchemaService teletextSourceService;

  @GetMapping("{source}")
  @Operation(
      summary = "Get teletext source schema",
      description = "Returns the configuration schema for the specified teletext source.")
  public ResponseEntity<ConfigSchemaResponse> getSourceSchema(@PathVariable TeletextSource source) {
    var result = teletextSourceService.getSchema(source);
    return ResponseEntity.ok(result);
  }

  @GetMapping
  @Operation(
      summary = "Get all teletext schemas",
      description = "Returns a list of all available teletext configuration schemas.")
  public ResponseEntity<List<ConfigSchemaResponse>> getAllSchemas() {
    var result = teletextSourceService.getAllSchemas();
    return ResponseEntity.ok(result);
  }
}
