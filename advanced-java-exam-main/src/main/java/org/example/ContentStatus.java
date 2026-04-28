package com.media.catalog.domain;

import java.util.Set;

public enum ContentStatus {
    DRAFT, REVIEW, PUBLISHED, ARCHIVED;

    public boolean canTransitionTo(ContentStatus next) {
        return switch (this) {
            case DRAFT -> Set.of(REVIEW, ARCHIVED).contains(next);
            case REVIEW -> Set.of(PUBLISHED, DRAFT).contains(next);
            case PUBLISHED -> Set.of(ARCHIVED).contains(next);
            case ARCHIVED -> false;
        };
    }
}