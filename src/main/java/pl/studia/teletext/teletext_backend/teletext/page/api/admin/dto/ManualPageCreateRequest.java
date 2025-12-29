package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.studia.teletext.teletext_backend.teletext.category.domain.TeletextCategory;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

public record ManualPageCreateRequest(
    @Min(value = 101, message = "Numer strony musi być większy niż {value}")
        @Max(value = 999, message = "Numer strony musi być mniejszy niż {value}")
        int pageNumber,
    @NotNull(message = "Należy podać kategorię strony") TeletextCategory category,
    @NotBlank(message = "Tytuł strony nie może być pusty") String title,
    @NotBlank(message = "Opis strony musi być wypełniony") String description)
    implements PageCreateRequest {
  @Override
  public TeletextAdminPageResponse handle(TeletextPageService service) {
    return service.createManualPage(this);
  }
}
