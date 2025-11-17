package pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations;

import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.NewsResponse;
import pl.studia.teletext.teletext_backend.clients.news.NewsDataResponse;

@Mapper(componentModel = "spring")
public interface NewsMapper {

  NewsResponse toResponse(NewsDataResponse.NewsArticle apiSingleResponse);
}
