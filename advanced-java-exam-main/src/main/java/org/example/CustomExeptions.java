package com.media.catalog.exception;

public class MediaCatalogException extends RuntimeException {
    public MediaCatalogException(String message) { super(message); }
}

public class LifecycleException extends MediaCatalogException {
    public LifecycleException(String message) { super(message); }
}