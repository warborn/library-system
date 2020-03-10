package classes;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB {
    
    private static final String db = "jdbc:mysql://localhost/library_system";
    private static final String username = "root";
    private static final String password = "secret";
    private Connection connection;
    
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(db, username, password);
    }
    
}