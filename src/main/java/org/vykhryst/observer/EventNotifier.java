package org.vykhryst.observer;

import org.vykhryst.observer.listeners.EntityEventListener;

import java.util.*;

public abstract class EventNotifier<T> {
    protected final Map<EntityEventListener<T>, Set<EventType>> listeners = new HashMap<>();

    public void subscribe(EntityEventListener<T> listener, EventType... eventTypes) {
        Set<EventType> subscribedEvents = new HashSet<>(Arrays.asList(eventTypes));
        listeners.put(listener, subscribedEvents);
    }

    public void subscribe(EntityEventListener<T> listener) {
        listeners.put(listener, EnumSet.allOf(EventType.class));
    }

    public void unsubscribe(EntityEventListener<T> listener) {
        listeners.remove(listener);
    }

    protected void notifyEntityAdded(T entity) {
        notifyListeners(entity, EventType.ADDED, EntityEventListener::onEntityAdded);
    }

    protected void notifyEntityUpdated(T entity) {
        notifyListeners(entity, EventType.UPDATED, EntityEventListener::onEntityUpdated);
    }

    protected void notifyEntityDeleted(long id) {
        listeners.forEach((listener, eventTypes) -> {
            if (eventTypes.contains(EventType.DELETED)) {
                listener.onEntityDeleted(id);
            }
        });
    }

    private void notifyListeners(T entity, EventType eventType, EntityEventInvoker<T> invoker) {
        listeners.forEach((listener, eventTypes) -> {
            if (eventTypes.contains(eventType)) {
                invoker.invoke(listener, entity);
            }
        });
    }

    @FunctionalInterface
    private interface EntityEventInvoker<T> {
        void invoke(EntityEventListener<T> listener, T entity);
    }
}
