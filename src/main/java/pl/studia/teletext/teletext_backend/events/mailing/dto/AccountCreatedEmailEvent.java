package pl.studia.teletext.teletext_backend.events.mailing.dto;

import java.time.LocalDateTime;

public record AccountCreatedEmailEvent(
    String email, String username, LocalDateTime createdAt, String createdBy) {}
