package com.media.catalog.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T> {
    void save(T item);
    Optional<T> findById(String id);
    List<T> findAll();
    List<T> findByCriteria(Predicate<T> filter);
}