package pl.studia.teletext.teletext_backend.integration.news.mapper;

import org.mapstruct.Mapper;
import pl.studia.teletext.teletext_backend.integration.news.domain.NewsDataResponse;
import pl.studia.teletext.teletext_backend.integration.news.domain.NewsResponse;

@Mapper(componentModel = "spring")
public interface NewsMapper {

  NewsResponse toResponse(NewsDataResponse.NewsArticle apiSingleResponse);
}
