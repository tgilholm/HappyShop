package ci553.happyshop.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the Derby DB by centralizing connection and config logic
 */
public class DatabaseConnection
{
    private final String dbURL; // jdbc:derby:happyShopDB

    /**
     * Constructs a DatabaseConnection object & sets dbURL
     * @param dbURL Location of derby DB
     */
    public DatabaseConnection(String dbURL) {this.dbURL = dbURL;}

    /**
     * Create a new DB connection
     * @return Connection object from <code>dbURL</code>
     * @throws SQLException if failed to connect to Derby
     */
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(dbURL);
    }

    /**
     * Get the database URL
     * @return the database URL
     */
    public String getDbURL() {return dbURL;}
}
