package org.vykhryst.observer.listeners;

public interface EntityEventListener<T> {
    void onEntityAdded(T entity);
    void onEntityDeleted(long entityId);
    void onEntityUpdated(T entity);
}
