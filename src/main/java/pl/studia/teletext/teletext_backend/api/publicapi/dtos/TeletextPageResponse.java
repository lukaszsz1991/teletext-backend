package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

import java.util.List;

public record TeletextPageResponse (
    Long id,
    Integer pageNumber,
    String title,
    String category,
    List<TeletextPageContentResponse> content,
    String createdAt,
    String updatedAt
){}
