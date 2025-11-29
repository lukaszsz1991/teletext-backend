package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import java.util.List;

public record TeletextDetailedPageResponse(
    Long id,
    Integer pageNumber,
    String title,
    TeletextCategoryResponse category,
    List<TeletextPageContentResponse> content,
    String createdAt,
    String updatedAt
){}
