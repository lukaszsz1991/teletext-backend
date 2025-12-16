package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ManualPageCreateRequest.class, name = "MANUAL"),
  @JsonSubTypes.Type(value = TemplatePageCreateRequest.class, name = "TEMPLATE")
})
public interface PageCreateRequest {
  TeletextAdminPageResponse handle(TeletextPageService service);
}
