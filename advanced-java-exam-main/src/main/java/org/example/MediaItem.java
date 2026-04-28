package com.media.catalog.domain;

import java.util.Objects;
import java.util.UUID;

public record MediaItem(String id, String title, Author author, ContentStatus status) {
    public MediaItem {
        Objects.requireNonNull(title);
        Objects.requireNonNull(author);
        Objects.requireNonNull(status);
    }

    public MediaItem(String title, Author author) {
        this(UUID.randomUUID().toString(), title, author, ContentStatus.DRAFT);
    }

    public MediaItem withStatus(ContentStatus newStatus) {
        return new MediaItem(this.id, this.title, this.author, newStatus);
    }
}