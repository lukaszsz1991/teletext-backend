package pl.studia.teletext.teletext_backend.teletext.page.mapper;

import org.mapstruct.*;
import pl.studia.teletext.teletext_backend.teletext.category.mapper.TeletextCategoryMapper;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.*;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.TemplatePageCreateRequest;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.TemplatePageUpdateRequest;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPage;

@Mapper(
    componentModel = "spring",
    uses = {
      TeletextPageMapper.class,
      TeletextCategoryMapper.class,
    })
public interface TeletextAdminPageMapper {

  @Mapping(target = "type", expression = "java(page.getType())")
  TeletextAdminPageResponse toResponse(TeletextPage page);

  @Mapping(target = "content.title", source = "title")
  @Mapping(target = "content.description", source = "description")
  @Mapping(target = "content.source", constant = "MANUAL")
  TeletextPage toPage(ManualPageCreateRequest request);

  @Mapping(target = "template", ignore = true)
  TeletextPage toPage(TemplatePageCreateRequest request);

  @AfterMapping
  default void linkContent(@MappingTarget TeletextPage page) {
    if (page.getContent() != null) {
      page.getContent().setPage(page);
    }
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "template", ignore = true)
  void updatePageFromTemplateRequest(
      TemplatePageUpdateRequest request, @MappingTarget TeletextPage page);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "content.title", source = "title")
  @Mapping(target = "content.description", source = "description")
  void updatePageFromManualRequest(
      ManualPageUpdateRequest request, @MappingTarget TeletextPage page);
}
