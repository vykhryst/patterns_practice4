package org.vykhryst.observer.listeners;

import org.vykhryst.entity.Client;

public class ClientEventListener implements EntityEventListener<Client> {

        @Override
        public void onEntityAdded(Client entity) {
            System.out.println("Client added: " + entity);
        }

        @Override
        public void onEntityDeleted(long entityId) {
            System.out.println("Client with id " + entityId + " was deleted");
        }

        @Override
        public void onEntityUpdated(Client entity) {
            System.out.println("Client updated: " + entity);
        }
}
