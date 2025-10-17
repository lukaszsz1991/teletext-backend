package pl.studia.teletext.teletext_backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageContentResponse;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.models.TeletextPage;
import pl.studia.teletext.teletext_backend.models.TeletextPageContent;

@Mapper(componentModel = "spring")
public interface TeletextPageMapper {

  TeletextPageContentResponse toContentResponse(TeletextPageContent content);

  @Mapping(target = "content", source = "content")
  TeletextPageResponse toPageResponse(TeletextPage page);
}
