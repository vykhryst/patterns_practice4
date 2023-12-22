package org.vykhryst.memento;

import org.vykhryst.dao.DAO;
import org.vykhryst.entity.User;

public class UserCaretaker implements Caretaker<User> {
    @Override
    public void save(User user, DAO<User> dao) {
        user.setId(dao.save(user));
        User.UserMemento memento = user.new UserMemento(user);
        memento.save();
    }

    @Override
    public void update(User user, DAO<User> dao) {
        dao.update(user);
        User.UserMemento memento = user.new UserMemento(user);
        memento.save();
    }

    @Override
    public void undo(User user, DAO<User> dao) {
        if (isDeleted(user, dao)) {
            System.out.println("The client was deleted. Can't undo");
            return;
        }

        if (user.getMementos().size() <= 1) {
            System.out.println("Can't undo: no more mementos");
            return;
        }

        User.UserMemento currentMemento = user.new UserMemento(user);
        User.UserMemento previousMemento = user.getMementos().get(user.getMementos().size() - 2);
        User userToRestore = User.UserMemento.fromMemento(previousMemento);

        dao.update(userToRestore);
        currentMemento.undo();
    }

    private boolean isDeleted(User user, DAO<User> dao) {
        return dao.findById(user.getId()).isEmpty();
    }
}
