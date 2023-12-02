package org.vykhryst.dao.mysqlEntityDao;


import org.vykhryst.util.DBException;
import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.entity.Program;
import org.vykhryst.entity.ProgramAdvertising;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlProgramDAO implements ProgramDAO {

    private static final String SELECT_PROGRAM_ADVERTISING_BY_ID = "SELECT a.id, a.name, pa.quantity, a.measurement, a.unit_price, a.description\n" +
            "FROM program p INNER JOIN program_advertising pa ON p.id = pa.program_id INNER JOIN advertising a ON pa.advertising_id = a.id WHERE p.id = ?";
    private static final String SELECT_FROM_PROGRAM = "SELECT * FROM program";
    private static final String SELECT_FROM_PROGRAM_BY_ID = "SELECT * FROM program WHERE id = ?";
    private static final String INSERT_PROGRAM = "INSERT INTO program (client_id) VALUES (?)";
    private static final String DELETE_PROGRAM_BY_ID = "DELETE FROM program WHERE id = ?";
    private static final String INSERT_PROGRAM_ADVERTISING = "INSERT INTO program_advertising (program_id, advertising_id, quantity) VALUES (?, ?, ?)";
    private static final String UPDATE_PROGRAM_QUANTITY = "UPDATE program_advertising SET quantity = ? WHERE program_id = ? AND advertising_id = ?";
    private static final String DELETE_PROGRAM_ADVERTISING = "DELETE FROM program_advertising WHERE program_id = ? AND advertising_id = ?";
    private final ConnectionManager connectionManager;

    public MySqlProgramDAO() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    @Override
    public List<Program> findAll() throws DBException {
        try (Connection conn = connectionManager.getConnection();
             Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_FROM_PROGRAM);
            List<Program> programs = new ArrayList<>();
            while (rs.next()) {
                Program program = mapProgram(rs);
                programs.add(program);
            }
            getProgramAdvertisings(conn, programs);
            return programs;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Program mapProgram(ResultSet rs) throws SQLException {
        return new Program.Builder()
                .id(rs.getInt("id"))
                .clientId(rs.getInt("client_id"))
                .build();
    }

    private static void getProgramAdvertisings(Connection conn, List<Program> programs) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement(SELECT_PROGRAM_ADVERTISING_BY_ID)) {
            for (Program program : programs) {
                st.setLong(1, program.getId());
                ResultSet rs2 = st.executeQuery();
                while (rs2.next()) {
                    ProgramAdvertising programAdvertising = mapProgramAdvertising(rs2);
                    program.addAdvertising(programAdvertising);
                }
            }
        }
    }

    private static ProgramAdvertising mapProgramAdvertising(ResultSet rs) throws SQLException {
        return new ProgramAdvertising.Builder()
                .advertisingId(rs.getInt("id"))
                .name(rs.getString("name"))
                .quantity(rs.getInt("quantity"))
                .measurement(rs.getString("measurement"))
                .unitPrice(rs.getBigDecimal("unit_price"))
                .description(rs.getString("description"))
                .build();
    }

    @Override
    public Optional<Program> findById(long id) throws DBException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement st = conn.prepareStatement(SELECT_FROM_PROGRAM_BY_ID)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Program program = mapProgram(rs);
                    getProgramAdvertisings(conn, List.of(program));
                    return Optional.of(program);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }


    @Override
    public long save(Program program) throws DBException {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = connectionManager.getConnection(false); // false - no auto commit
            // insert program
            st = conn.prepareStatement(INSERT_PROGRAM, Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, program.getClientId());
            st.executeUpdate();
            // get generated id
            ResultSet programKeys = st.getGeneratedKeys();
            // insert program advertisings
            st = conn.prepareStatement(INSERT_PROGRAM_ADVERTISING);
            if (programKeys.next()) {
                program.setId(programKeys.getLong(1));
                for (ProgramAdvertising programAdvertising : program.getAdvertising()) {
                    st.setLong(1, program.getId());
                    st.setLong(2, programAdvertising.getAdvertisingId());
                    st.setInt(3, programAdvertising.getQuantity());
                    st.addBatch();
                }
                st.executeBatch();
            }
            connectionManager.commit(conn); // commit transaction
            return program.getId();
        } catch (SQLException e) {
            connectionManager.rollback(conn); // rollback transaction
            throw new DBException(e.getMessage(), e);
        } finally {
            connectionManager.close(conn, st); // close connection and statement
        }
    }

    @Override
    public boolean saveAdvertisingToProgram(long programId, ProgramAdvertising... programAdvertising) throws DBException {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = connectionManager.getConnection(false); // false - no auto commit
            // insert program advertisings
            st = conn.prepareStatement(INSERT_PROGRAM_ADVERTISING);
            // iterate over program advertisings
            for (ProgramAdvertising advertising : programAdvertising) {
                st.setLong(1, programId);
                st.setLong(2, advertising.getAdvertisingId());
                st.setInt(3, advertising.getQuantity());
                st.executeUpdate();
            }
            connectionManager.commit(conn); // commit transaction
            return true;
        } catch (SQLException e) {
            connectionManager.rollback(conn); // rollback transaction
            throw new DBException(e.getMessage());
        } finally {
            connectionManager.close(conn, st); // close connection and statement
        }
    }

    @Override
    public boolean update(Program program) throws DBException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement st = conn.prepareStatement(UPDATE_PROGRAM_QUANTITY)) {
            for (ProgramAdvertising programAdvertising : program.getAdvertising()) {
                st.setInt(1, programAdvertising.getQuantity());
                st.setLong(2, program.getId());
                st.setLong(3, programAdvertising.getAdvertisingId());
                st.addBatch();
            }
            return st.executeBatch().length > 0;
        } catch (SQLException e) {
            throw new DBException("Can't update program", e);
        }
    }

    @Override
    public boolean delete(long id) throws DBException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement st = conn.prepareStatement(DELETE_PROGRAM_BY_ID)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't delete program", e);
        }
    }

    @Override
    public boolean deleteAdvertisingFromProgram(long programId, long advertisingId) throws DBException {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement st = conn.prepareStatement(DELETE_PROGRAM_ADVERTISING)) {
            st.setLong(1, programId);
            st.setLong(2, advertisingId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException("Can't delete advertising from program", e);
        }
    }
}
