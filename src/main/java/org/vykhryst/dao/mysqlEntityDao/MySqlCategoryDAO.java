package org.vykhryst.dao.mysqlEntityDao;

import org.vykhryst.dao.entityDao.CategoryDAO;
import org.vykhryst.entity.Category;
import org.vykhryst.observer.EventNotifier;
import org.vykhryst.util.DBException;

import java.sql.*;
import java.util.*;

public class MySqlCategoryDAO extends EventNotifier<Category> implements CategoryDAO {
    private static final String SELECT_ALL = "SELECT id, name FROM category";
    private static final String INSERT_CATEGORY = "INSERT INTO category (name) VALUES (?)";
    private static final String DELETE_BY_ID = "DELETE FROM category WHERE id = ?";
    public static final String SELECT_BY_ID = "SELECT id, name FROM category WHERE id = ?";
    public static final String UPDATE_CATEGORY = "UPDATE category SET name = ? WHERE id = ?";
    private final ConnectionManager connectionManager;

    public MySqlCategoryDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public Optional<Category> findById(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapCategory(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException("Can't get category by id", e);
        }
    }

    @Override
    public List<Category> findAll() {
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            List<Category> result = new ArrayList<>();
            while (rs.next()) {
                Category category = mapCategory(rs);
                result.add(category);
            }
            return result;
        } catch (SQLException e) {
            throw new DBException("Can't get all categories", e);
        }
    }

    private static Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt(1));
        category.setName(rs.getString(2));
        return category;
    }

    @Override
    public long save(Category category) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
            ResultSet programKeys = stmt.getGeneratedKeys();
            if (programKeys.next()) {
                category.setId(programKeys.getInt(1));
            }
            // Додано сповіщення про додавання сутності
            notifyEntityAdded(category);
            return category.getId();
        } catch (SQLException e) {
            throw new DBException("Can't insert category", e);
        }
    }

    @Override
    public boolean update(Category entity) {
        try(Connection conn = connectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)) {
            stmt.setString(1, entity.getName());
            stmt.setLong(2, entity.getId());
            // Додано сповіщення про оновлення сутності
            notifyEntityUpdated(entity);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't update category", e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID)) {
            stmt.setLong(1, id);
            // Додано сповіщення про видалення сутності
            notifyEntityDeleted(id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't delete category", e);
        }
    }

}
