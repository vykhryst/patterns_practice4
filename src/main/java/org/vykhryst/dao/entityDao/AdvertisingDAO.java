package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Advertising;

import java.sql.SQLException;
import java.util.Optional;

public interface AdvertisingDAO extends DAO<Advertising> {
    Optional<Advertising> findByName(String name) throws SQLException;
}
