package pl.studia.teletext.teletext_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.studia.teletext.teletext_backend.services.TeletextPageService;

@Controller
@RequestMapping("/api/pages")
@RequiredArgsConstructor
public class TeletextPageController {

  private final TeletextPageService teletextPageService;

  @GetMapping
  public ResponseEntity<?> getAllPages() {
    return ResponseEntity.ok(teletextPageService.getAllPagesWithContent());
  }

  @GetMapping("{pageNumber}")
  public ResponseEntity<?> getPageByNumber(
    @PathVariable Integer pageNumber
  ) {
    return ResponseEntity.ok(teletextPageService.getPageWithContent(pageNumber));
  }
}
