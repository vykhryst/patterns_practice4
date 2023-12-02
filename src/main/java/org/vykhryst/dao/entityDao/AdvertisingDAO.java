package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Advertising;
import org.vykhryst.entity.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AdvertisingDAO extends DAO<Advertising> {
    List<Category> findAllCategories() throws SQLException;

    boolean saveCategory(Category category) throws SQLException;

    boolean deleteCategory(long id) throws SQLException;

    Optional<Advertising> findByName(String name) throws SQLException;
}
