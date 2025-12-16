package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextCategoryResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;

@Mapper(componentModel = "spring")
public interface TeletextCategoryMapper {

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "category.title")
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category, int nextFreePage);

  @Mapping(target = "originalName", expression = "java(category.name())")
  @Mapping(target = "category", source = "title")
  @Mapping(target = "nextFreePage", ignore = true)
  TeletextCategoryResponse toCategoryResponse(TeletextCategory category);
}
