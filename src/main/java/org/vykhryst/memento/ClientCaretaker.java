package org.vykhryst.memento;

import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Client;

public class ClientCaretaker implements Caretaker<Client> {
    @Override
    public void save(Client client, DAO<Client> dao) {
        client.setId(dao.save(client));
        Client.ClientMemento memento = client.new ClientMemento(client);
        memento.save();
    }

    @Override
    public void update(Client client, DAO<Client> dao) {
        dao.update(client);
        Client.ClientMemento memento = client.new ClientMemento(client);
        memento.save();
    }

    @Override
    public void undo(Client client, DAO<Client> dao) {
        if (isDeleted(client, dao)) {
            System.out.println("The client was deleted. Can't undo");
            return;
        }

        if (client.getMementos().size() <= 1) {
            System.out.println("Can't undo: no more mementos");
            return;
        }

        Client.ClientMemento currentMemento = client.new ClientMemento(client);
        Client.ClientMemento previousMemento = client.getMementos().get(client.getMementos().size() - 2);
        Client clientToRestore = Client.ClientMemento.fromMemento(previousMemento);

        dao.update(clientToRestore);
        currentMemento.undo();
    }

    private boolean isDeleted(Client client, DAO<Client> dao) {
        return dao.findById(client.getId()).isEmpty();
    }
}
