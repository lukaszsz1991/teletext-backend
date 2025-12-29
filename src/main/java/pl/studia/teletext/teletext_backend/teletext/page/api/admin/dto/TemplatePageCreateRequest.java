package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

public record TemplatePageCreateRequest(
    @Min(value = 101, message = "Numer strony musi być większy niż {value}")
        @Max(value = 999, message = "Numer strony musi być mniejszy niż {value}")
        int pageNumber,
    @NotNull(message = "Należy podać kategorię strony") TeletextCategory category,
    @NotNull(message = "Należy wybrać szablon strony") long templateId)
    implements PageCreateRequest {
  @Override
  public TeletextAdminPageResponse handle(TeletextPageService service) {
    return service.createTemplatePage(this);
  }
}
