package pl.studia.teletext.teletext_backend.api.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.admin.dtos.stats.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.domain.services.TeletextPageStatsService;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@Tag(name = "Pages stats viewer controller", description = "Operations for Teletext page stats.")
public class AdminStatsController {

  private final TeletextPageStatsService teletextPageStatsService;

  //TODO: add all pages stats sorted by views desc

  @Operation(summary = "Get all views of page", description = "Returns counted views. Optionally include details about each view. If details not included return empty list in \"stats\" field.")
  @ApiResponse(responseCode = "200", description = "Page found, stats returned successfully.")
  @GetMapping("pages/{pageNumber}")
  public ResponseEntity<TeletextPageStatsResponse> getPageStats(
    @PathVariable Integer pageNumber,
    @RequestParam (required = false, defaultValue = "false") Boolean includeDetails
  ) {
    var page = teletextPageStatsService.getStatsForPage(pageNumber, includeDetails);
    return ResponseEntity.ok(page);
  }
}
