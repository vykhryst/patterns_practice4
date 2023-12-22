package org.vykhryst.dao;

import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.dao.entityDao.UserDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;

public interface DAOFactory {
    AdvertisingDAO getAdvertisingDAO();
    UserDAO getUserDAO();
    ProgramDAO getProgramDAO();

    CategoryDAO getCategoryDAO();
}
