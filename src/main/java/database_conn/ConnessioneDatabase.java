package database_conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnessioneDatabase {

    private static final Logger LOGGER = Logger.getLogger(ConnessioneDatabase.class.getName());

    private static ConnessioneDatabase instance;

    private static final String URL = "jdbc:postgresql://localhost:5432/ospedale_db";
    private static final String USER = "postgres";
    private static final String DRIVER = "org.postgresql.Driver";

    private Connection connection = null;

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    private ConnessioneDatabase() throws SQLException {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException("ERRORE: La variabile d'ambiente 'DB_PASSWORD' non è configurata!");
        }

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, password);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Database Connection Creation Failed", ex);
            throw new SQLException("Driver PostgreSQL non trovato", ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}