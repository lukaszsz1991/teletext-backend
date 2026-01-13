package pl.studia.teletext.teletext_backend.teletext.template.mapper;

import org.mapstruct.*;
import pl.studia.teletext.teletext_backend.teletext.page.mapper.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.teletext.template.api.dto.TeletextPageFullTemplateResponse;
import pl.studia.teletext.teletext_backend.teletext.template.api.dto.TeletextPageTemplateCreateRequest;
import pl.studia.teletext.teletext_backend.teletext.template.api.dto.TeletextPageTemplateResponse;
import pl.studia.teletext.teletext_backend.teletext.template.api.dto.TeletextPageTemplateUpdateRequest;
import pl.studia.teletext.teletext_backend.teletext.template.domain.TeletextPageTemplate;

@Mapper(
    componentModel = "spring",
    uses = {TeletextPageMapper.class})
public interface TeletextPageTemplateMapper {

  @Mapping(target = "category", expression = "java(template.getCategory().name())")
  TeletextPageTemplateResponse toResponse(TeletextPageTemplate template);

  @Mapping(target = "category", expression = "java(template.getCategory().name())")
  TeletextPageFullTemplateResponse toFullResponse(TeletextPageTemplate template);

  TeletextPageTemplate toTemplate(TeletextPageTemplateCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateTemplateFromRequest(
      TeletextPageTemplateUpdateRequest request, @MappingTarget TeletextPageTemplate template);
}
