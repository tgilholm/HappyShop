package ci553.happyshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the Derby DB by centralizing connection and config logic.
 * Declared as record as this class purely carries data for use in other classes
 *
 * @param dbURL the database URL
 */
public record DatabaseConnection(String dbURL)
{
    /**
     * Create a new DB connection
     *
     * @return Connection object from <code>dbURL</code>
     * @throws SQLException if failed to connect to Derby
     */
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(dbURL);
    }

    /**
     * Get the database URL
     *
     * @return the database URL
     */
    @Override
    public String dbURL()
    {
        return dbURL;
    }
}
