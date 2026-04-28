package com.media.catalog.service;

import com.media.catalog.api.Repository;
import com.media.catalog.domain.ContentStatus;
import com.media.catalog.domain.MediaItem;
import com.media.catalog.exception.LifecycleException;
import com.media.catalog.exception.MediaCatalogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class CatalogService {
    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);
    private final Repository<MediaItem> repository;

    public CatalogService(Repository<MediaItem> repository) {
        this.repository = repository;
    }

    public void registerMedia(MediaItem item) {
        if (item.title().length() < 3) {
            log.error("Валідація не вдалась: Занадто коротке імʼя [{}]", item.title());
            throw new MediaCatalogException("Має бути щонайменше 3 символи");
        }
        repository.save(item);
        log.info("Медіа зареєстоване: {} (Статус: {})", item.title(), item.status());
    }

    public void updateStatus(String id, ContentStatus newStatus) {
        MediaItem item = repository.findById(id)
                .orElseThrow(() -> new MediaCatalogException("Айтем не знайдений"));

        if (!item.status().canTransitionTo(newStatus)) {
            log.warn("Нелегальна спроба дозвілу статусу: {} -> {}", item.status(), newStatus);
            throw new LifecycleException("Не може перейти з  " + item.status() + " до " + newStatus);
        }

        repository.save(item.withStatus(newStatus));
        log.info("Оновнений статус {}: {}", id, newStatus);
    }

    // Stream-запит (R1)
    public List<MediaItem> getPublishedSortedByTitle() {
        return repository.findAll().stream()
                .filter(i -> i.status() == ContentStatus.PUBLISHED)
                .sorted(Comparator.comparing(MediaItem::title))
                .toList();
    }
}