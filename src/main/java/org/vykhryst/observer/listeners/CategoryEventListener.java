package org.vykhryst.observer.listeners;

import org.vykhryst.entity.Category;

public class CategoryEventListener implements EntityEventListener<Category> {

    @Override
    public void onEntityAdded(Category entity) {
        System.out.println("Category with id " + entity.getId() + " and name '" + entity.getName() + "' was added");
    }

    @Override
    public void onEntityDeleted(long entityId) {
        System.out.println("Category with id " + entityId + " was deleted");
    }

    @Override
    public void onEntityUpdated(Category entity) {
        System.out.println("Category with id " + entity.getId() + " and name '" + entity.getName() + "' was updated");
    }
}
