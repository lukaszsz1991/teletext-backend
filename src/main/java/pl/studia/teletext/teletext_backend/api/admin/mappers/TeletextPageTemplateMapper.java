package pl.studia.teletext.teletext_backend.api.admin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageTemplate;

@Mapper(componentModel = "spring")
public interface TeletextPageTemplateMapper {

  @Mapping(target = "category", expression = "java(template.getCategory().name())")
  TeletextPageTemplateResponse toResponse(TeletextPageTemplate template);
}
