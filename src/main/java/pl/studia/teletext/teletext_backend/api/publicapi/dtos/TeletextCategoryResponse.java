package pl.studia.teletext.teletext_backend.api.publicapi.dtos;

public record TeletextCategoryResponse(
    String originalName, String category, String description, int mainPage, int nextFreePage) {}
