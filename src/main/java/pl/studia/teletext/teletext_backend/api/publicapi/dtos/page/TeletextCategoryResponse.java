package pl.studia.teletext.teletext_backend.api.publicapi.dtos.page;

public record TeletextCategoryResponse(
    String originalName, String category, String description, int mainPage, Integer nextFreePage) {}
