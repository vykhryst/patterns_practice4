package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Program;
import org.vykhryst.entity.ProgramAdvertising;

import java.sql.SQLException;

public interface ProgramDAO extends DAO<Program> {
    boolean saveAdvertisingToProgram(long programId, ProgramAdvertising... programAdvertising) throws SQLException;

    boolean deleteAdvertisingFromProgram(long programId, long advertisingId) throws SQLException;
}
