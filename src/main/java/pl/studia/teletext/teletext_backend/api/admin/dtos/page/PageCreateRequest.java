package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

public interface PageCreateRequest {
  int pageNumber();

  TeletextCategory category();

  TeletextDetailedPageResponse handle(TeletextPageService service);
}
