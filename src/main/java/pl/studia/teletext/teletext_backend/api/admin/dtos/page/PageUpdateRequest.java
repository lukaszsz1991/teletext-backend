package pl.studia.teletext.teletext_backend.api.admin.dtos.page;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageService;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ManualPageUpdateRequest.class, name = "MANUAL"),
  @JsonSubTypes.Type(value = TemplatePageUpdateRequest.class, name = "TEMPLATE")
})
public interface PageUpdateRequest {
  TeletextAdminPageResponse handle(Long id, TeletextPageService service);
}
