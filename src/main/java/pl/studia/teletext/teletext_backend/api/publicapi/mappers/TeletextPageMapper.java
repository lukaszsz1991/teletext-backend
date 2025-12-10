package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.ManualPageCreateRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.page.TemplatePageCreateRequest;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextFullPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageContent;

@Mapper(componentModel = "spring", uses = TeletextCategoryMapper.class)
public interface TeletextPageMapper {

  @Mapping(target = "title", expression = "java(page.getTitle())")
  TeletextPageResponse toPageResponse(TeletextPage page);

  TeletextDetailedPageResponse toDetailedPageResponse(TeletextPage page);

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "source", expression = "java(content.getSource().getName())")
  TeletextFullPageContentResponse toFullContentResponse(TeletextPageContent content);

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
}
