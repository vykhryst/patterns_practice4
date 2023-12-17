package org.vykhryst.dao.entityDao;


import org.vykhryst.dao.DAO;
import org.vykhryst.entity.Advertising;
import org.vykhryst.entity.Program;

import java.sql.SQLException;
import java.util.Map;

public interface ProgramDAO extends DAO<Program> {
    boolean saveAdvertisingToProgram(long programId, Map<Advertising, Integer> advertising) throws SQLException;

    boolean deleteAdvertisingFromProgram(long programId, long advertisingId) throws SQLException;
}
