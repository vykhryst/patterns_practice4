package org.vykhryst.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> findById(long id) throws SQLException;

    List<T> findAll() throws SQLException;

    long save(T entity) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(long id) throws SQLException;

//    void subscribe(EntityEventListener<T> listener, EventType... eventTypes);
//    void unsubscribe(EntityEventListener<T> listener);
}
