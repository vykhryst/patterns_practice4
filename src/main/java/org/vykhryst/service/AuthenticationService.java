package org.vykhryst.service;

import org.vykhryst.dao.entityDao.UserDAO;
import org.vykhryst.entity.User;

public class AuthenticationService {
    private final UserDAO userDao;

    public AuthenticationService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String username, String password) {
        return userDao.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new SecurityException("Invalid credentials"));
    }

    public User register(User user) {
        long id = userDao.save(user);
        return userDao.findById(id).orElseThrow(() -> new SecurityException("Can't register user"));
    }
}
