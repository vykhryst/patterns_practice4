package org.vykhryst.proxy;

import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.entity.Advertising;
import org.vykhryst.entity.User;

import java.util.List;
import java.util.Optional;

public class ProxyAdvertisingDAO extends ProxyDAO implements AdvertisingDAO {

    private final AdvertisingDAO advertisingDao;

    public ProxyAdvertisingDAO(User credentials, AdvertisingDAO advertisingDao) {
        super(credentials);
        this.advertisingDao = advertisingDao;
    }

    @Override
    public Optional<Advertising> findById(long id) {
        return advertisingDao.findById(id);
    }

    @Override
    public List<Advertising> findAll() {
        return advertisingDao.findAll();
    }

    @Override
    public long save(Advertising entity) {
        if (hasAccess()) return advertisingDao.save(entity);
        return -1;
    }

    @Override
    public boolean update(Advertising entity) {
        if (hasAccess()) return advertisingDao.update(entity);
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (hasAccess()) return advertisingDao.delete(id);
        return false;
    }

    @Override
    public Optional<Advertising> findByName(String name) {
        return advertisingDao.findByName(name);
    }
}
