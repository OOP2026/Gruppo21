package implementazioneDao;

import dao.DAO;
import database_connection.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicoImplementazioneDAO implements DAO {
    private Connection connection;

    public MedicoImplementazioneDAO() throws SQLException {
        connection = ConnessioneDatabase.getConnection();
    }

    public void fetchDB() throws SQLException {

    }

    @Override
    public Boolean verificaCredenziali(String email, String password) throws SQLException {
        String SQL = "SELECT * FROM medico WHERE email = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
