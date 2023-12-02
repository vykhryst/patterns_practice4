package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Client;
import org.vykhryst.util.DBException;

import java.util.Optional;

public interface ClientDAO extends DAO<Client> {
    Optional<Client> findByUsername(String username) throws DBException;
}
