package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPageContent;

@Mapper(componentModel = "spring")
public interface TeletextPageMapper {

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "content", source = "content")
  TeletextPageResponse toPageResponse(TeletextPage page);
}
