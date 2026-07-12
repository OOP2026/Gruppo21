package implementazione_dao;

import database_conn.ConnessioneDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class connection {

    private connection() {}

    public static Connection connect() {
        Connection connection = null;

        try { connection = ConnessioneDatabase.getInstance().getConnection(); }
        catch (SQLException e) { throw new RuntimeException(e); }

        return connection;
    }

    public static boolean sqlVerificaCredenziali(Connection connection, String email, String password, boolean tipo) throws SQLException {
        String sql = null;

        if(tipo) {
            sql = "SELECT 1 FROM Amministratore WHERE email = ? AND password = ?";
        } else sql = "SELECT 1 FROM Medico WHERE email = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email); ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
