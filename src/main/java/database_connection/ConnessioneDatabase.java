package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase instance;
    private static String URL = "jdbc:postgresql://localhost:5432/ospedale_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "25aprogrammazione";
    private String driver = "org.postgresql.Driver";

    public Connection connection = null;

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        } return instance;
    }


    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed :" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}