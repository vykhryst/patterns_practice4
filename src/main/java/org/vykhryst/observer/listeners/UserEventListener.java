package org.vykhryst.observer.listeners;

import org.vykhryst.entity.User;

public class UserEventListener implements EntityEventListener<User> {

        @Override
        public void onEntityAdded(User entity) {
            System.out.println("Client added: " + entity);
        }

        @Override
        public void onEntityDeleted(long entityId) {
            System.out.println("Client with id " + entityId + " was deleted");
        }

        @Override
        public void onEntityUpdated(User entity) {
            System.out.println("Client updated: " + entity);
        }
}
