package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.*;
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
