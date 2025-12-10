package pl.studia.teletext.teletext_backend.api.admin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageFullTemplateResponse;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TeletextPageTemplateResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.TeletextPageTemplate;

@Mapper(componentModel = "spring", uses = TeletextPageMapper.class)
public interface TeletextPageTemplateMapper {

  @Mapping(target = "category", expression = "java(template.getCategory().name())")
  TeletextPageTemplateResponse toResponse(TeletextPageTemplate template);

  @Mapping(target = "category", expression = "java(template.getCategory().name())")
  TeletextPageFullTemplateResponse toFullResponse(TeletextPageTemplate template);

  TeletextPageTemplate toTemplate(TeletextPageTemplateCreateRequest request);
}
