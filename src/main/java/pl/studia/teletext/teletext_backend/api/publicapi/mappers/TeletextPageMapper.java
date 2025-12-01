package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextFullPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageContent;

@Mapper(componentModel = "spring")
public interface TeletextPageMapper {

  @Mapping(target = "title", expression = "java(mapTitle(page))")
  TeletextPageResponse toPageResponse(TeletextPage page);

  TeletextDetailedPageResponse toDetailedPageResponse(TeletextPage page);

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  TeletextFullPageContentResponse toFullContentResponse(TeletextPageContent content);

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "category.title")
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category, int nextFreePage);

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "title")
  @Mapping(target = "nextFreePage", ignore = true)
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category);

  @Named("mapTitle")
  default String mapTitle(TeletextPage page) {
    return page.getContent() != null
        ? page.getContent().getTitle()
        : "Strona dynamiczna: " + page.getTemplate().getName();
  }
}
