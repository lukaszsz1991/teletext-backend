package pl.studia.teletext.teletext_backend.events.mailing;

import java.time.LocalDateTime;

public record PasswordChangedEmailEvent(String email, LocalDateTime changedAt, String changedBy) {}
