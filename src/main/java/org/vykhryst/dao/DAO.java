package org.vykhryst.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    Optional<T> findById(long id);

    List<T> findAll();

    long save(T entity);

    boolean update(T entity);

    boolean delete(long id);
}
