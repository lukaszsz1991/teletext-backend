package pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.studia.teletext.teletext_backend.teletext.page.service.TeletextPageService;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ManualPageCreateRequest.class, name = "MANUAL"),
  @JsonSubTypes.Type(value = TemplatePageCreateRequest.class, name = "TEMPLATE")
})
public interface PageCreateRequest {
  TeletextAdminPageResponse handle(TeletextPageService service);
}
