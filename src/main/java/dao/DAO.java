package dao;

import java.sql.SQLException;

public interface DAO {
    public Boolean verificaCredenziali(String email, String password) throws SQLException;
}
