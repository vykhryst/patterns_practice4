package org.vykhryst.dao.mysqlEntityDao;


import org.vykhryst.dao.entityDao.ClientDAO;
import org.vykhryst.entity.Client;
import org.vykhryst.observer.EventNotifier;
import org.vykhryst.observer.listeners.EntityEventListener;
import org.vykhryst.util.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlClientDAO extends EventNotifier<Client> implements ClientDAO {

    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM client";
    private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM client WHERE id = ?";
    public static final String SELECT_CLIENT_BY_USERNAME = "SELECT * FROM client WHERE username = ?";
    private static final String INSERT_CLIENT = "INSERT INTO client (username, firstname, lastname, phone_number, email, password) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CLIENT = "UPDATE client SET username = ?, firstname = ?, lastname = ?, phone_number = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_CLIENT_BY_ID = "DELETE FROM client WHERE id = ?";
    private final ConnectionManager connectionManager;

    public MySqlClientDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public List<Client> findAll() {
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CLIENTS)) {
            List<Client> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapClient(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DBException("Can't get all clients", e);
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        return new Client.Builder().id(rs.getInt("id"))
                .username(rs.getString("username"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .phoneNumber(rs.getString("phone_number"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .build();
    }

    @Override
    public Optional<Client> findById(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CLIENT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapClient(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException("Can't get client by id", e);
        }
    }

    @Override
    public long save(Client client) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            setClientStatement(client, stmt);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    client.setId(keys.getLong(1));
                }
            }
            notifyEntityAdded(client);
            return client.getId();
        } catch (SQLException e) {
            throw new DBException("Can't insert client", e);
        }
    }

    private static void setClientStatement(Client client, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, client.getUsername());
        stmt.setString(2, client.getFirstname());
        stmt.setString(3, client.getLastname());
        stmt.setString(4, client.getPhoneNumber());
        stmt.setString(5, client.getEmail());
        stmt.setString(6, client.getPassword());
    }

    @Override
    public boolean update(Client client) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CLIENT)) {
            setClientStatement(client, stmt);
            stmt.setLong(7, client.getId());
            notifyEntityUpdated(client);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't update client", e);
        }

    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CLIENT_BY_ID)) {
            stmt.setLong(1, id);
            notifyEntityDeleted(id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't delete client", e);
        }
    }


    @Override
    public Optional<Client> findByUsername(String username) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CLIENT_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapClient(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException("Can't get client by username", e);
        }
    }
}
