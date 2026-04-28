package com.media.catalog.domain;

import java.util.List;

public record Author(String id, String name) {}

public record MediaCollection(String id, String name, List<MediaItem> items) {}