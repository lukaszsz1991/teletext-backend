package pl.studia.teletext.teletext_backend.teletext.stats.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.teletext.stats.api.dto.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.teletext.stats.service.TeletextPageStatsService;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@Tag(name = "Pages stats viewer controller", description = "Operations for Teletext page stats.")
public class AdminTeletextPageStatsController {

  private final TeletextPageStatsService teletextPageStatsService;

  @Operation(
      summary = "Get all pages stats",
      description =
          "Returns counted views for each page in selected date range. Optionally with details.")
  @ApiResponse(responseCode = "200", description = "Pages stats returned successfully.")
  @GetMapping("pages")
  public ResponseEntity<List<TeletextPageStatsResponse>> getPagesStats(
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "1", name = "page") Integer pageNumber,
      @Parameter(
              description =
                  "Start date for filtering stats (inclusive), preferred format: YYYY-MM-DD. If not passed then the first day of the current month is used.")
          @RequestParam(required = false)
          LocalDate fromDate,
      @Parameter(
              description =
                  "End date for filtering stats (inclusive), preferred format: YYYY-MM-DD. If not passed then current date is used.")
          @RequestParam(required = false)
          LocalDate toDate,
      @RequestParam(defaultValue = "false") Boolean includeDetails) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, size);
    var page =
        teletextPageStatsService.getAllPagesStats(pageRequest, includeDetails, fromDate, toDate);
    return ResponseEntity.ok(page);
  }

  @Operation(
      summary = "Get all stats of page",
      description = "Returns counted views. Optionally include details about each view.")
  @ApiResponse(responseCode = "200", description = "Page found, stats returned successfully.")
  @GetMapping("pages/{pageNumber}")
  public ResponseEntity<TeletextPageStatsResponse> getPageStats(
      @PathVariable Integer pageNumber,
      @RequestParam(defaultValue = "false") Boolean includeDetails) {
    var page = teletextPageStatsService.getStatsForPage(pageNumber, includeDetails);
    return ResponseEntity.ok(page);
  }
}
