package org.vykhryst.observer.listeners;

import org.vykhryst.entity.Program;

public class ProgramEventListener implements EntityEventListener<Program> {

        @Override
        public void onEntityAdded(Program entity) {
            System.out.println("Program added: " + entity);
        }

        @Override
        public void onEntityDeleted(long entityId) {
            System.out.println("Program with id " + entityId + " was deleted");
        }

        @Override
        public void onEntityUpdated(Program entity) {
            System.out.println("Program updated: " + entity);
        }
}
