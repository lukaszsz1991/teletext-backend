package pl.studia.teletext.teletext_backend.events.stats;

import java.time.Instant;

public record PageViewedEvent(Long pageId, Instant occurredAt) {}
