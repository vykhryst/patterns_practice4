package org.vykhryst.proxy;

import org.vykhryst.dao.entityDao.ProgramDAO;
import org.vykhryst.entity.Advertising;
import org.vykhryst.entity.Program;
import org.vykhryst.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProxyProgramDAO extends ProxyDAO implements ProgramDAO {

    private final ProgramDAO programDao;

    public ProxyProgramDAO(User credentials, ProgramDAO programDao) {
        super(credentials);
        this.programDao = programDao;
    }

    @Override
    public Optional<Program> findById(long id) {
        return programDao.findById(id);
    }

    @Override
    public List<Program> findAll() {
        return programDao.findAll();
    }

    @Override
    public long save(Program entity) {
        if (hasAccess()) return programDao.save(entity);
        return -1;
    }

    @Override
    public boolean update(Program entity) {
        if (hasAccess()) return programDao.update(entity);
        return false;
    }

    @Override
    public boolean delete(long id) {
        if (hasAccess()) return programDao.delete(id);
        return false;
    }

    @Override
    public boolean saveAdvertisingToProgram(long programId, Map<Advertising, Integer> advertising) {
        if (hasAccess()) return programDao.saveAdvertisingToProgram(programId, advertising);
        return false;
    }

    @Override
    public boolean deleteAdvertisingFromProgram(long programId, long advertisingId) {
        if (hasAccess()) return programDao.deleteAdvertisingFromProgram(programId, advertisingId);
        return false;
    }
}
