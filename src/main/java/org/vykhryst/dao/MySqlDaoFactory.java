package org.vykhryst.dao;

import org.vykhryst.dao.entityDao.AdvertisingDAO;
import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.dao.entityDao.ClientDAO;
import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlAdvertisingDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlCategoryDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlClientDAO;
import org.vykhryst.dao.mysqlEntityDao.MySqlProgramDAO;

public class MySqlDaoFactory implements DAOFactory {

    private static MySqlDaoFactory instance = new MySqlDaoFactory();

    private MySqlDaoFactory() {
        // Приватний конструктор, щоб заборонити створення екземплярів
    }

    public static MySqlDaoFactory getInstance() {
        if (instance == null) {
            instance = new MySqlDaoFactory();
        }
        return instance;
    }

    public AdvertisingDAO getAdvertisingDAO() {
        return new MySqlAdvertisingDAO();
    }

    public ClientDAO getClientDAO() {
        return new MySqlClientDAO();
    }

    public ProgramDAO getProgramDAO() {
        return new MySqlProgramDAO();
    }

    public CategoryDAO getCategoryDAO() {
        return new MySqlCategoryDAO();
    }

}
