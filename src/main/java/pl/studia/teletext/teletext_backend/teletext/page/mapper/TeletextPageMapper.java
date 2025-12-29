package pl.studia.teletext.teletext_backend.teletext.page.mapper;

import org.mapstruct.*;
import pl.studia.teletext.teletext_backend.teletext.category.mapper.TeletextCategoryMapper;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextFullPageContentResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.teletext.page.api.publicapi.dto.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPage;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextPageContent;

@Mapper(componentModel = "spring", uses = TeletextCategoryMapper.class)
public interface TeletextPageMapper {

  @Mapping(target = "title", expression = "java(page.getTitle())")
  TeletextPageResponse toPageResponse(TeletextPage page);

  @Mapping(target = "type", expression = "java(page.getType())")
  TeletextDetailedPageResponse toDetailedPageResponse(TeletextPage page);

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "source", source = "content", qualifiedByName = "getSource")
  TeletextFullPageContentResponse toFullContentResponse(TeletextPageContent content);

  @Named("getSource")
  default String getSource(TeletextPageContent content) {
    return content.getSource().getName();
  }
}
