package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ManualPageUpdateRequest.class, name = "MANUAL"),
  @JsonSubTypes.Type(value = TemplatePageUpdateRequest.class, name = "TEMPLATE")
})
public interface PageUpdateRequest {
  TeletextAdminPageResponse handle(Long id, TeletextPageService service);
}
