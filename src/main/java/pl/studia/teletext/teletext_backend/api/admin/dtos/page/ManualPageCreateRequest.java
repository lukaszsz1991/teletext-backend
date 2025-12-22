package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

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
