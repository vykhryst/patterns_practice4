package org.vykhryst.observer.listeners;

import org.vykhryst.entity.Advertising;

public class AdvertisingEventListener implements EntityEventListener<Advertising> {

    @Override
    public void onEntityAdded(Advertising entity) {
        System.out.println("Advertising added: " + entity);
    }

    @Override
    public void onEntityDeleted(long entityId) {
        System.out.println("Advertising with id " + entityId + " was deleted");
    }

    @Override
    public void onEntityUpdated(Advertising entity) {
        System.out.println("Advertising updated: " + entity);
    }
}
