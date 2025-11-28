package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPageContent;

@Mapper(componentModel = "spring")
public interface TeletextPageMapper {

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "content", source = "content")
  TeletextPageResponse toPageResponse(TeletextPage page);

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "category.title")
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category, int nextFreePage);

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "title")
  @Mapping(target = "nextFreePage", ignore = true)
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category);
}
