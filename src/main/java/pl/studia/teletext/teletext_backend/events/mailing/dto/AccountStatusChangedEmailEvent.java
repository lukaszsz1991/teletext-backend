package pl.studia.teletext.teletext_backend.events.mailing.dto;

import java.time.LocalDateTime;

public record AccountStatusChangedEmailEvent(
    String email, boolean isActive, LocalDateTime changedAt, String changedBy) {}
