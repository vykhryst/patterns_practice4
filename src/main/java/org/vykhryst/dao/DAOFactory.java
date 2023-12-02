package org.vykhryst.dao;

import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.ClientDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;

public interface DAOFactory {
    AdvertisingDAO getAdvertisingDAO();
    ClientDAO getClientDAO();
    ProgramDAO getProgramDAO();
}
