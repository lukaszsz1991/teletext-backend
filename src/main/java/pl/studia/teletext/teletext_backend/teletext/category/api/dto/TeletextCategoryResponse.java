package pl.studia.teletext.teletext_backend.teletext.category.api.dto;

public record TeletextCategoryResponse(
    String originalName, String category, String description, int mainPage, Integer nextFreePage) {}
