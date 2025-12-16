package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

public record TemplatePageUpdateRequest(
    @Min(value = 101, message = "Numer strony musi być większy niż 100")
        @Max(value = 999, message = "Numer strony musi być mniejszy niż 1000")
        int pageNumber,
    @NotNull(message = "Należy podać kategorię strony") TeletextCategory category,
    @NotNull(message = "Należy wybrać szablon strony") long templateId)
    implements PageUpdateRequest {
  @Override
  public TeletextAdminPageResponse handle(Long id, TeletextPageService service) {
    return service.updateTemplatePage(id, this);
  }
}
