package org.vykhryst.proxy;

import org.vykhryst.dao.entityDao.UserDAO;
import org.vykhryst.entity.User;

import java.util.List;
import java.util.Optional;

public class ProxyUserDAO extends ProxyDAO implements UserDAO {

    private final UserDAO userDao;

    public ProxyUserDAO(User credentials, UserDAO userDao) {
        super(credentials);
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public long save(User entity) {
        if (hasAccess()) return userDao.save(entity);
        return -1;
    }

    @Override
    public boolean update(User entity) {
        if (hasAccess()) return userDao.update(entity);
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (hasAccess()) return userDao.delete(id);
        return false;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
