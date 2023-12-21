package org.vykhryst.memento;

import org.vykhryst.dao.DAO;

public interface Caretaker<T> {

    void save(T entity, DAO<T> dao);

    void update(T entity, DAO<T> dao);

    void undo(T entity, DAO<T> dao);
}
