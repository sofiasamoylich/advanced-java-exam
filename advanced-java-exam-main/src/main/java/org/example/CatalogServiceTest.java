package com.media.catalog;

import com.media.catalog.domain.*;
import com.media.catalog.exception.*;
import com.media.catalog.service.CatalogService;
import com.media.catalog.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceTest {
    private CatalogService service;
    private Author author = new Author("1", "Test Author");

    @BeforeEach
    void setUp() {
        service = new CatalogService(new InMemoryRepository<>());
    }

    @Test
    void testSuccessfulRegistration() {
        MediaItem item = new MediaItem("Valid Title", author);
        assertDoesNotThrow(() -> service.registerMedia(item));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", ""})
    void testNegativeTitleValidation(String shortTitle) {
        MediaItem item = new MediaItem(shortTitle, author);
        assertThrows(MediaCatalogException.class, () -> service.registerMedia(item));
    }

    @Test
    void testNegativeIllegalStatusTransition() {
        MediaItem item = new MediaItem("Test", author); // DRAFT
        service.registerMedia(item);
        // DRAFT -> PUBLISHED is forbidden (must go through REVIEW)
        assertThrows(LifecycleException.class, () -> service.updateStatus(item.id(), ContentStatus.PUBLISHED));
    }

    @Test
    void testNegativeItemNotFound() {
        assertThrows(MediaCatalogException.class, () -> service.updateStatus("non-existent", ContentStatus.REVIEW));
    }

    @Test
    void testStatusTransitionDraftToReview() {
        MediaItem item = new MediaItem("Test Item", author);
        service.registerMedia(item);
        assertDoesNotThrow(() -> service.updateStatus(item.id(), ContentStatus.REVIEW));
    }
}