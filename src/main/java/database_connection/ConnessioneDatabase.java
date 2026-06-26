package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {


    private static final String URL = "jdbc:postgresql://localhost:5432/nome_del_tuo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "la_tua_password";


    private static Connection connessione = null;


    private ConnessioneDatabase() {}


    public static Connection getConnessione() throws SQLException {
        if (connessione == null || connessione.isClosed()) {
            try {

                Class.forName("org.postgresql.Driver");
                connessione = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connessione al database stabilita con successo!");
            } catch (ClassNotFoundException e) {
                System.err.println("Errore: Driver PostgreSQL non trovato!");
                throw new SQLException(e);
            }
        }
        return connessione;
    }


    public static void chiudiConnessione() {
        if (connessione != null) {
            try {
                connessione.close();
                System.out.println("Connessione al database chiusa.");
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}