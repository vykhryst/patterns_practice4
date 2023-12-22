package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.User;

import java.util.Optional;

public interface UserDAO extends DAO<User> {
    Optional<User> findByUsername(String username);
}
