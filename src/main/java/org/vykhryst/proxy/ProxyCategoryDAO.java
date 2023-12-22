package org.vykhryst.proxy;

import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.entity.Category;
import org.vykhryst.entity.User;

import java.util.List;
import java.util.Optional;

public class ProxyCategoryDAO extends ProxyDAO implements CategoryDAO {

    private final CategoryDAO categoryDao;

    public ProxyCategoryDAO(User credentials, CategoryDAO categoryDao) {
        super(credentials);
        this.categoryDao = categoryDao;
    }

    @Override
    public Optional<Category> findById(long id) {
        return categoryDao.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public long save(Category entity) {
        if (hasAccess()) return categoryDao.save(entity);
        return -1;
    }

    @Override
    public boolean update(Category entity) {
        if (hasAccess()) return categoryDao.update(entity);
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (hasAccess()) return categoryDao.delete(id);
        return false;
    }
}
