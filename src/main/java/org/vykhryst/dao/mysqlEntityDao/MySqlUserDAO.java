package org.vykhryst.dao.mysqlEntityDao;


import org.vykhryst.dao.entityDao.UserDAO;
import org.vykhryst.entity.Role;
import org.vykhryst.entity.User;
import org.vykhryst.observer.EventNotifier;
import org.vykhryst.util.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlUserDAO extends EventNotifier<User> implements UserDAO {

    private static final String SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
    public static final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
    private static final String INSERT_USER = "INSERT INTO user (role, username, firstname, lastname, phone_number, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE user SET role = ?, username = ?, firstname = ?, lastname = ?, phone_number = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM user WHERE id = ?";
    private final ConnectionManager connectionManager;

    public MySqlUserDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public List<User> findAll() {
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {
            List<User> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapUser(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DBException("Can't get all users", e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User.Builder().id(rs.getInt("id"))
                .role(Role.valueOf(rs.getString("role").toUpperCase()))
                .username(rs.getString("username"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .phoneNumber(rs.getString("phone_number"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .build();
    }

    @Override
    public Optional<User> findById(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException("Can't get user by id", e);
        }
    }

    @Override
    public long save(User user) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            setUserStatement(user, stmt);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
            notifyEntityAdded(user);
            return user.getId();
        } catch (SQLException e) {
            throw new DBException("Can't insert user", e);
        }
    }

    private static void setUserStatement(User user, PreparedStatement stmt) throws SQLException {
        int i = 0;
        stmt.setString(++i, user.getRole().getName().toUpperCase());
        stmt.setString(++i, user.getUsername());
        stmt.setString(++i, user.getFirstname());
        stmt.setString(++i, user.getLastname());
        stmt.setString(++i, user.getPhoneNumber());
        stmt.setString(++i, user.getEmail());
        stmt.setString(++i, user.getPassword());
    }

    @Override
    public boolean update(User user) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            setUserStatement(user, stmt);
            stmt.setLong(8, user.getId());
            notifyEntityUpdated(user);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't update user", e);
        }

    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_BY_ID)) {
            stmt.setLong(1, id);
            notifyEntityDeleted(id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't delete user", e);
        }
    }


    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException("Can't get user by username", e);
        }
    }
}
